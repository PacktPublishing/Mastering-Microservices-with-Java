angular.module('otrsApp.login', [
    'ui.router',
    'ngStorage'
])
        .config(function config($stateProvider) {
            $stateProvider.state('login', {
                url: '/login',
                controller: 'LoginCtrl',
                templateUrl: 'login/login.html'
            });
        })
        .controller('LoginCtrl', function ($state, $scope, $rootScope, $injector) {
            var $sessionStorage = $injector.get('$sessionStorage');
            if ($sessionStorage.currentUser) {
                $state.go($rootScope.fromState.name, $rootScope.fromStateParams);
            }
            var controller = this;
            var log = $injector.get('$log');
            var http = $injector.get('$http');

            $scope.$on('$destroy', function destroyed() {
                log.debug('LoginCtrl destroyed');
                controller = null;
                $scope = null;
            });

            //http.get('https://localhost:9001' + '/auth/oauth/authorize?response_type=token&redirect_uri=http://localhost:1337/index.html&scope=apiAccess&state=553344&client_id=client', null);

            this.cancel = function () {
                $scope.$dismiss;
                $state.go('restaurants');
            }
            console.log("Current --> " + $state.current);
            this.submit = function (username, password) {
                $rootScope.currentUser = username;
                $sessionStorage.currentUser = username;
                if ($rootScope.fromState.name) {
                    $state.go($rootScope.fromState.name, $rootScope.fromStateParams);
                } else {
                    $state.go("restaurants");
                }
            };
        });