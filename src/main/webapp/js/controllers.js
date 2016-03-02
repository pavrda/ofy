myApp.controller('emptyController', [ '$scope', 
    function($scope) {
}]);


myApp.controller('nedeleController', [ '$scope', '$http', 
function ($scope, $http) {

    $scope.balance = "";
    $scope.seznamHracu = "";

    $http({
        method: 'GET',
        url: '/api/login?username=pavrda&password=ahoj'
     }).success(function(data){
         
         $http({
             method: 'GET',
             url: '/api/gameListWeek'
          }).success(function(data){
        	  $scope.seznamHracu = data.listPlayers.join();
        	  $scope.balance = data.credit;
        	  $scope.listGames = data.listGames;
        	  $scope.lastRound = data.lastRound;
         }).error(function(){
             alert("error");
         });
    	 
    }).error(function(){
        alert("error");
    });

    
    $scope.rozdelKredit = function () {
        $http({
            method: 'GET',
            url: '/api/rozdelKredit',
            params: {
            	players: $scope.seznamHracu,
				credit: $scope.balance                		
            }
         }).success(function(data){
        	 alert("Rozdeleno");
        	 location.reload();
        }).error(function(){
            alert("error");
        });
    }
    
}]);
