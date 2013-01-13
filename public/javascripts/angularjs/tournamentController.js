function TournamentController($scope,WebSocket) {
    $scope.messageboxshow = false;
    $scope.messageboxerror = false;
    $scope.messageboxmessage = '';
    $scope.buttonLoading = false;

    $scope.allPlayers = [];

    $scope.createTournament = function () {
        var tournament = $scope.tournament;
        var json = {method:'CREATE_TOURNAMENT',tournamentName: tournament.name,selectedPlayers:tournament.selectedPlayers};
        var message = angular.toJson(json);
        $('#sButton').button('loading');
        $scope.buttonLoading = true;
        WebSocket.send(message);
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
            case 'SIMPLE_RESPONSE':
                if($scope.buttonLoading){
                    $('#sButton').button('reset');
                    $scope.buttonLoading = false;
                }
                if (protocolContainer.successful) {
                    $scope.resetForm();
                    $scope.messageboxshow = true;
                    $scope.messageboxmessage = protocolContainer.successMessage;
                    $scope.messageboxerror = false;
                }else{
                    $scope.messageboxshow = true;
                    $scope.messageboxmessage = protocolContainer.errorMessage;
                    $scope.messageboxerror = true;
                }

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