var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var games = [];
var players = [];
var waitingPlayers = [];
var gameCount = 0;

server.listen(8080, function(){
    console.log("Server is now running...");
});

io.on('connection', function(socket){
    console.log("Player Connected: "+socket.id);
    socket.emit('socketID', {id: socket.id});
    socket.on('disconnect', function(){
        console.log("Player Disconnected: "+socket.id);
        for(var i = 0;i < players.length;i++){
            if(players[i].id == socket.id){
                players.splice(i, 1);
            }
        }
        for(var i = 0;i < waitingPlayers.length;i++){
            if(waitingPlayers[i].id == socket.id){
                waitingPlayers.splice(i, 1);
            }
        }
        for(var i = 0;i < games.length;i++){
            if(io.nsps['/'].adapter.rooms[games[i]] == undefined){
                games.splice(i, 1);
            }
        }
    });
    socket.on('newGame', function(data){
        var gameName = "game" + gameCount;
        gameCount++;
        var g = new game(gameName, data.seed, data.mapSize, data.armySize, data.teamCount, data.fogOfWar);
        games.push(g);
        socket.join(gameName);
        socket.gameName = gameName;
        console.log(gameName + " created.");
        while(waitingPlayers.length > 0 &&  io.nsps['/'].adapter.rooms[gameName].length < g.teamCount){
            var p = waitingPlayers.shift(); 
            p.gameName = gameName;
            io.sockets.connected[p.id].join(gameName);
            p.emit("joinGame", toJSON(g));
            console.log(p.id + " joining "+ gameName);
        }
        if(io.nsps['/'].adapter.rooms[gameName].length == g.teamCount){
            io.to(gameName).emit("startGame", io.nsps['/'].adapter.rooms[gameName].sockets);
            console.log(gameName + " is full, starting game."); 
        }
    });
    socket.on('findGame', function(){
        for(var i = 0; i < games.length;i++){
            if(io.nsps['/'].adapter.rooms[games[i].name].length < games[i].teamCount){
                socket.join(games[i].name);
                socket.gameName = games[i].name;
                socket.emit("joinGame", toJSON(games[i]));
                console.log("Game found: " + socket.id + " joining " + games[i].name);
                if(io.nsps['/'].adapter.rooms[games[i].name].length == games[i].teamCount){
                    io.to(games[i].name).emit("startGame", io.nsps['/'].adapter.rooms[games[i].name].sockets);
                    console.log(games[i].name + " is full, starting game.");
                }
                return;
            }
        }
        console.log("No Games found set " + socket.id + " to waiting.");
        waitingPlayers.push(socket);
    });
    socket.on('actionSend', function(data){
        socket.to(socket.gameName).emit("actionRecieve", data);
        console.log(socket.id + " took action in "+socket.gameName);
    });
    players.push(socket);
});

function game(name, seed, mapSize, armySize, teamCount, fogOfWar){
    this.name = name;
    this.seed = seed;
    this.mapSize = mapSize;
    this.armySize = armySize;
    this.teamCount = teamCount;
    this.fogOfWar = fogOfWar;
}

function toJSON(game){
    return {seed: game.seed, mapSize: game.mapSize, armySize: game.armySize, teamCount: game.teamCount, fogOfWar: game.fogOfWar};
}