function TournamentController($scope,WebSocket) {
    $scope.messageboxshow = false;
    $scope.messageboxerror = false;
    $scope.messageboxmessage = '';


    $scope.allPlayers = [];

    $scope.createTournament = function () {
        var tournament = $scope.tournament;
        var json = {method:'CREATE_TOURNAMENT',tournamentName: tournament.name,selectedPlayers:tournament.selectedPlayers};
        var message = angular.toJson(json);
        WebSocket.send(message);
        $scope.resetForm();
    };

    $scope.changeSelection = function () {
         if ($scope.tournament.selectedPlayers.length < 4) {
            $scope.tournamentForm.selectedPlayers.$setValidity('tooless',false);
        } else if ($scope.tournament.selectedPlayers.length % 2 != 0) {
            $scope.tournamentForm.selectedPlayers.$setValidity('noteven',false);
        }else{
             $scope.tournamentForm.selectedPlayers.$setValidity('tooless',true);
             $scope.tournamentForm.selectedPlayers.$setValidity('noteven',true);
        }
    }

    $scope.resetForm = function(){
        $scope.tournamentForm.selectedPlayers.$setValidity('tooless',true);
        $scope.tournamentForm.selectedPlayers.$setValidity('noteven',true);
        $scope.tournament = null;
    }
    WebSocket.onmessage = function (event) {
        var protocolContainer = angular.fromJson(event.data);
        switch (protocolContainer.method) {
            case 'ALL_PLAYER':
                $scope.allPlayers = protocolContainer.players;
                break;
            case 'TRIGGER_RELOAD':
                $scope.fetchAllPlayers();
                break;
        }
        $scope.$digest();
    }

    $scope.fetchAllPlayers = function(){
        var request = {method:'ALL_PLAYER'};
        WebSocket.send(JSON.stringify(request));
    }


    url = jsRoutes.controllers.tournament.creation.TournamentCreationController.websocket().webSocketURL();
    WebSocket.onopen = function () {
        $scope.fetchAllPlayers();
    }
    WebSocket.connect(url);


}