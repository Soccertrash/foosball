var tournamentManagerModule = angular.module('tournamentManager', []);

tournamentManagerModule.directive('bootstrapinput', function ($compile) {
    return {
        restrict:'E',
        compile:function (tElement, tAttrs) {
            var type = tAttrs.type || 'text';
            var required = tAttrs.hasOwnProperty('required') ? 'required' : '';
            var errorMessage = tAttrs.hasOwnProperty('formpath') ? '<span class="help-inline" ng-show="' + tAttrs.formpath + '.$error.required">' + tAttrs.errormessage + '</span>' : '';
            var ngClazz = tAttrs.hasOwnProperty('formpath') ? 'ng-class="{error: ' + tAttrs.formpath + '.$invalid}"' : '';
            var html = '<div><div class="control-group" ' + ngClazz + '>' +
                '<label class="control-label" for="' + tAttrs.key + '">' + tAttrs.localized + '</label>' +
                '<div class="controls">' +
                '<input ng-model="' + tAttrs.model + '" type="' + type + '" id="' + tAttrs.key + '" name="' + tAttrs.key + '" placeholder="' + tAttrs.localized + '" ' + required + '   />' +
                errorMessage +
                '</div>' +
                '</div></div>';

            tElement.replaceWith(html);


        }
    };

});


tournamentManagerModule.
    config(function ($routeProvider) {
        $routeProvider.
            when('/player', {templateUrl:'partials/player.html'}).
            when('/tournamentCreation', {templateUrl:'partials/tournametCreation.html'}).
            otherwise({redirectTo:'/home', templateUrl:'partials/home.html'});
    });


tournamentManagerModule.factory('WebSocket', function () {
    return{
        connect:function (url) {
            var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
            this.webSock = new WS(url);
            var self = this;
            this.webSock.onmessage = function (event) {
                if (self.onmessage) {
                    self.onmessage(event);
                }
            }

            this.webSock.onopen = function () {
                if (self.onopen) {
                    self.onopen();
                }
            }


        },
        send:function (message) {
            this.webSock.send(message);
        }

    }

});
