

function NavigationController($scope, $location) {
    $scope.setRoute = function (route) {
        $location.path(route)

    }
    $scope.isActive = function (route) {
        return route === $location.path();
    }
}

function PlayerController($scope, WebSocket) {

    $scope.players = [];

    $scope.messageboxshow = false;
    $scope.messageboxerror = false;
    $scope.messageboxmessage = '';
    $scope.deleteplayerid = -1;

    $scope.buttonLoading = false;

    $scope.addPlayer = function () {
        var playerOnly = $scope.player;
        $('#sButton').button('loading');
        $scope.buttonLoading = true;
        var protocolContainer = {method:"CREATE_PLAYER", player:playerOnly};
        var jsonPlayer = JSON.stringify(protocolContainer);
        WebSocket.send(jsonPlayer);

    };


    $scope.currentPage = 0;
    $scope.pageSize = 5;

    $scope.amountPlayers = 0;

    $scope.requestPage = function () {
        var start = $scope.currentPage * $scope.pageSize;
        var end = $scope.pageSize;
        var request = {method:'PAGED_PLAYER', maxRows:end, from:start};
        WebSocket.send(JSON.stringify(request));
    }


    $scope.setPage = function (page) {
        if (page >= 0 && page < $scope.maxPage()) {
            $scope.currentPage = page;
            $scope.updatePagination();
        }

    }

    $scope.pagesAmount = function () {
        var ret = [];
        for (i = 1; i <= $scope.maxPage(); i++) {
            ret.push(i);
        }
        return ret;

    };

    $scope.maxPage = function () {
        return Math.ceil($scope.amountPlayers / $scope.pageSize)
    };

    $scope.resetForm = function () {
        $scope.player = null;
    }

    $scope.querySize = function () {
        var protocolContainer = {method:"AMOUNT_PLAYER"};
        WebSocket.send(JSON.stringify(protocolContainer));
    }


    $scope.updatePagination = function () {
        $scope.querySize();
    }


    WebSocket.onmessage = function (event) {
        var protocolContainer = angular.fromJson(event.data);
        switch (protocolContainer.method) {
            case 'ENTITY_LIST':
                $scope.players = protocolContainer.entities;
                break;
            case 'PAGED_PLAYER':

                break;
            case 'CREATE_PLAYER':

                break;
            case 'SIMPLE_RESPONSE':
                if (protocolContainer.successful) {
                    $scope.updatePagination();
                    $scope.player = null;
                    $scope.messageboxshow = true;
                    $scope.messageboxmessage = protocolContainer.successMessage;
                    $scope.messageboxerror = false;
                }else{
                    $scope.messageboxshow = true;
                    $scope.messageboxmessage = protocolContainer.errorMessage;
                    $scope.messageboxerror = true;
                }
                if($scope.buttonLoading){
                    $('#sButton').button('reset');
                    $scope.buttonLoading = false;
                }

                break;
            case 'AMOUNT_PLAYER':
                // We got the size of the players
                $scope.amountPlayers = protocolContainer.size;
                $scope.requestPage();
                break;
            case 'TRIGGER_RELOAD':
                $scope.updatePagination();
                break;
        }
        $scope.$digest();
    };

    $scope.prepareDeletePlayer = function (playerId) {
        var jquery = $('#deleteConfirmation');
        $scope.deleteplayerid = playerId;
        jquery.modal('show');
    }

    $scope.deletePlayer = function(){
        var request = {method:'DELETE_PLAYER', playerId:$scope.deleteplayerid};
        WebSocket.send(JSON.stringify(request));
        $('#deleteConfirmation').modal('hide');
        $scope.deleteplayerid = -1;
    }




    url = jsRoutes.controllers.player.PlayerController.websocket().webSocketURL();
    WebSocket.onopen = function () {
        $scope.updatePagination();
    }
    WebSocket.connect(url);


}
;
