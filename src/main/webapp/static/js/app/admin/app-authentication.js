(function(){
	 var adminApp = angular.module('admin');

	 adminApp.controller('AdminAuthenticationController', ['$http', '$scope', function($http, $scope, Upload){
		 var adminAuth = this; 
		 var messageDiv = angular.element( document.querySelector( '#messageDiv' ) );
		  $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
		  // Override $http service's default transformRequest
		  $http.defaults.transformRequest = [function(data) {
		    return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
		  }];
		 $scope.countries = [{"name": "Loading", "abbr": "Loading"}];
		 $scope.message = "";
		 adminAuth.user = {"_csrf": "", "country": "", "company": "", "confirm_passwd": "", "passwd": "", "email_confirm": "", "email": "", "lastname": "", "firstname": ""};
		 $scope.isAuthenticated = false;
		 $http.get('/adminAuth/authenticated').success(function(response){
			 if(response.result != "false"){
				 $scope.isAuthenticated = true;
			 } 
		 }); 
	 
		 $scope.showMessage = function(){
			 return $scope.message.length > 0;
		 }
		 
		 $scope.auth = function(){ 
			  
			 if(!$scope.authForm.$valid){
				 messageDiv.removeClass("alert-success");
				 messageDiv.addClass("alert-danger");
				 $scope.message = "Please enter all required fields";
			 }else {
				 $http({
					 method: "POST",
					 url: '/admin/login',  
					 data: {username: adminAuth.user.email, password: adminAuth.user.passwd },
					 headers: {
						   'Accept': 'application/json',
						   "X-Requested-With": 'XMLHttpRequest',
					 },
				 }).success(function(response){
					 console.log("here");	
					  
					 window.location = "/admin/index";
						 
				}).error(function(response){
					if(response.error){
						messageDiv.removeClass("alert-success");
						 messageDiv.addClass("alert-danger");
						$scope.message = response.message;
					}
					 
				}); 
			 }
		 } 
	 }]);
       
	 	 
})();
  