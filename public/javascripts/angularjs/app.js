/**
 * The Directive module
 */
angular.module('tournamentmanager.directive', []).
    directive('bootstrapinput',function ($compile) {
        return {
            restrict:'E',
            compile:function (tElement, tAttrs) {
                var type = tAttrs.type || 'text';
                var required = tAttrs.hasOwnProperty('required') ? 'required' : '';
                var errorMessage = tAttrs.hasOwnProperty('formpath') ? '<span class="help-inline" ng-show="' + tAttrs.formpath + '.$error.required" i18n="' + tAttrs.errormessage + '"></span>' : '';
                var ngClazz = tAttrs.hasOwnProperty('formpath') ? 'ng-class="{error: ' + tAttrs.formpath + '.$invalid}"' : '';
                var html = '<div><div class="control-group" ' + ngClazz + '>' +
                    '<label class="control-label" for="' + tAttrs.key + '" i18n="'+ tAttrs.localized + '"></label>' +
                    '<div class="controls">' +
                    '<input ng-model="' + tAttrs.model + '" type="' + type + '" id="' + tAttrs.key + '" name="' + tAttrs.key + '" i18n="[placeholder]'+ tAttrs.localized + '" ' + required + '   />' +
                    errorMessage +
                    '</div>' +
                    '</div></div>';

                tElement.replaceWith(html);


            }
        };

    }).
    directive('i18n', function () {
        return {
            restrict:'A',
            link:function postLink(scope, iElement, iAttrs) {
                iElement.i18n();
            }
        }
    });


/**
 * The Filter module
 */
angular.module('tournamentmanager.filter', []);
/**
 * The Service module
 */
angular.module('tournamentmanager.service', []);


/**
 * The Factory module
 */
angular.module('tournamentmanager.factory', []).factory('WebSocket', function () {
    return{
        connect:function (url) {
            this.close();
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
            this.webSock.onclose = function () {
                if (self.onclose) {
                    self.onclose();
                }
            }


        },
        send:function (message) {
            this.webSock.send(message);
        },
        close:function () {
            if (this.webSock) {
                this.webSock.close();
            }
        }

    }

});

/**
 * The main module
 */
angular.module('tournamentmanager', ['tournamentmanager.service', 'tournamentmanager.directive', 'tournamentmanager.factory', 'tournamentmanager.filter']).
    config(function ($routeProvider) {
        $routeProvider.
            when('/home', {templateUrl:'partials/home.html'}).
            when('/player', {templateUrl:'partials/player.html'}).
            when('/tournamentCreation', {templateUrl:'partials/tournametCreation.html'}).
            otherwise({redirectTo:'/home'});
    }).
    run(function () {
        $.i18n.init({
            ns:{namespaces:['messages'], defaultNs:'messages'},
            debug:true,
            getAsync:false,
            selectorAttr:'i18n'
        }, function () {

        });
    });



function NavigationController($scope, $location) {
    $scope.setRoute = function (route) {
        $location.path(route)

    }
    $scope.isActive = function (route) {
        return route === $location.path();
    }
}
