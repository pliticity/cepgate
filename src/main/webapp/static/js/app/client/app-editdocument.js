(function(){
	var documentApp = angular.module('document');
	documentApp.controller('ADocumentController', ['$http', '$scope', 'Upload', function($http, $scope, Upload){
		 var aDoc = this; 
		 
		 aDoc.responseMessage = "";
		 aDoc.thisId = aDoc.currentId; 
		 $scope.newDocument = aDoc.newDocument; 
		 aDoc.anEditDocument = $scope.editDocument; 
		 $scope.createMessage = "";
		 aDoc.docTab = "in_progress_revision";
		 $scope.saveMessage = "";
		 var saveMessageDiv = angular.element(document.querySelector( '#saveMessageDiv' )); 
		 var createMessageDiv = angular.element(document.querySelector( '#createMessageDiv' ));
		 
		 $http.get('/signup/authenticated').success(function(response){
			 console.log('app-editdocument-authenticated');
			 if(response.result == "false"){
				 window.location = "/signup/login";
			 } 
		 });
		 
		 aDoc.showDocTab = function(theTab, $event){
			 console.log($($event.currentTarget));
			 $($event.currentTarget).addClass("active").siblings().removeClass("active");
			 aDoc.docTab = theTab;
		 }
		 
		 aDoc.shouldShowDocTab = function(theTab){
			
			 return aDoc.docTab == theTab;
		 }
		 
		 $scope.shouldShowMessage = function(theType){
			 if(theType == "save"){
				 return $scope.saveMessage.length > 0;
			 }else {
				 return $scope.createMessage.length > 0;
			 }
		 } 
		 
		 aDoc.hasResponseMessage = function(){
			 return aDoc.responseMessage.length > 0;
		 } 
		 
		 aDoc.hasSaveMessage = function(){
			 return $scope.saveMessage.length > 0;
		 } 
		 
		 aDoc.downloadFile = function(aFile) {
			 $http.get('/documents/canDownload/' + aFile.documentId ).success(function(response){
				 if(response.can_download == "true"){
					window.location = "/documents/download/" + aFile.id;	
				}else {
					alert(response.description);
				}
			});
		 } 
		 
		 aDoc.downloadDocument = function(aDocument) {
			 $http.get('/documents/canDownload/' + aDocument.id).success(function(response){
				 if(aDocument.files.length > 0 && response.can_download == "true"){
					 var allIds = [];
					 
					 for(var i = 0; i < aDoc.currentDownloadFiles.length ; i++){
						 allIds.push(JSON.parse(aDoc.currentDownloadFiles[i]).documentId); 
					 }
					 
					 if(allIds.length == 0){
						 alert("Please select at least one file to download.");
					 }else {
						 var theIds = allIds.join(",");
						  
						 window.location = "/documents/downloadDocument/" + theIds;
						 
					 }
				 }else if(response.can_download == "false"){
						 alert(response.description);
				 }
				
			});
		 } 
		 
		 aDoc.exportDocument = function(aDocument) {
			 $http.get('/documents/canDownload/' + aDocument.id ).success(function(response){
				 
				 if(response.can_download == "true"){
					window.location = "/documents/export/" + response.id;	
				}else {
					alert(response.description);
				}
			});
		 } 
		 
		 aDoc.removeFromArray = function(theArray, removeThis){
			 var toReturn = [];
			 for(var i = 0; i < theArray.length; i++){
				 if(theArray[i] != removeThis){
					 toReturn.push(theArray[i]);
				 }
			 }
			 return toReturn;
		 }
		 
		 aDoc.removeFile = function(actionType, aFile){
			 if(actionType == "edit"){
				 aDoc.anEditDocument.files = aDoc.removeFromArray(aDoc.anEditDocument.files, aFile);
			 }else {
				 aDoc.newDocument.files = aDoc.removeFromArray(aDoc.newDocument.files, aFile);
			 }
		 }
		 
		 $scope.addDocument = function() {
			 if(!aDoc.newDocument.files || aDoc.newDocument.files.length == 0){
				 var files = [aDoc.getNewFile()];
				 aDoc.newDocument.files = files;
			 } else {
				 aDoc.newDocument.files.push(aDoc.getNewFile()); 
			 }
			 $scope.newDocument = aDoc.newDocument; 
		 } 
		 
		 aDoc.editAddDocument = function() {
			  
			 if(!aDoc.anEditDocument.files || aDoc.anEditDocument.files.length == 0){
				 var files = [aDoc.getNewFile()];
				 aDoc.anEditDocument.files = files;
			 }else {
				 aDoc.anEditDocument.files.push(aDoc.getNewFile()); 
			 } 
			 aDoc.anEditDocument.masterDocumentNo = padDigits(aDoc.anEditDocument.masterDocumentNo, 8);
			 //$scope.anEditDocument = aDoc.anEditDocument; 
		 } 
		 
		 aDoc.toggleFavorite = function(aDocument){
			 $http.get('/documents/toggleFavorite/' + aDocument.id ).success(function(response){
				 $scope.$parent.reloadDocuments();
			}); 
		 } 
		 
		 aDoc.closeTab = function(aDocument){
			 console.log("D:" + aDoc.anEditDocument.documentNumber);
			 $('#link_' + aDoc.anEditDocument.documentNumber).remove();
			 $('#edit_' + aDoc.anEditDocument.documentNumber).remove();
			 //("display", "none"); 
			 //$("#edit_container_link").css("display", "none"); 
		     $('#home_link').click(); 
		 }
		 
		 aDoc.deleteDocument = function(aDocument){
			 if(confirm("Are you sure you would like to delete the document?")){
				 $http.get('/documents/delete/' + aDocument.id ).success(function(response){
					 alert(response.description);
					 $scope.$parent.reloadDocuments();
				}); 
			 } 
		 }
		 aDoc.getNewFile = function(){
			 var aFile = {"fileName": ""};
			 return aFile;
		 }
		 aDoc.cancel = function(){
			 $scope.$parent.reloadDocuments();
		 }
		 $scope.movedElement = function(theElement){
			 console.log(theElement);
		 }
		 aDoc.saveDocuments = function(theType){ 
			 var messages = "";
			 var aADoc = aDoc.newDocument;
			 var isGood = $scope.documentCreateForm.$valid; 
			 
			 if(theType == "save"){
				 aADoc = aDoc.anEditDocument;
				 aDoc.documentEditForm = $scope.$eval("documentEditForm" + aDoc.anEditDocument.documentNumber); 
				 isGood = aDoc.documentEditForm.$valid; 
			 } 
			  
			 if(!isGood){
				 if(theType == "save"){
					 saveMessageDiv.removeClass("alert-success");
					 saveMessageDiv.addClass("alert-danger");
					 $scope.saveMessage = "Please enter all required fields";
					 
				 }else {
					 saveMessageDiv.removeClass("alert-success");
					 saveMessageDiv.addClass("alert-danger");
					 $scope.createMessage = "Please enter all required fields";
				 }
			 }
			 
			 if(isGood && aADoc.documentName && aADoc.documentName.length > 300){
				 messages += "Document Name is too long. Please limit it to 300 characters\n";
				 isGood = false;
			 } 
			 
			 var theSaveItem = aADoc;
			 var theURL = '/documents/create';
			 if(theType == "save"){
				 theURL = '/documents/save'; 
			 }
			 var theFiles = theSaveItem.files; 
			 
			 if(isGood){ 
				 if(theType == "save"){
					 $scope.saveMessage = "";
				 }else {
					 $scope.createMessage = "";
				 }
				 var tags = "";
				 for(var i = 0; i < theSaveItem.tags.length; i++){
					 tags += theSaveItem.tags[i].text +","; 
				 }
				 theSaveItem.tags =  tags;
				 console.log(theSaveItem);
				 Upload.upload({
			            url: theURL,
			            data:  theSaveItem
			        }).then(function (resp) {
			        	alert(resp.data.description);
			        	console.log('Success ' + 'uploaded. Response: ' + resp.data.description);
			        	$scope.$parent.reloadDocuments();
			            if(theType == "save"){
			            	 $http.get('/documents/getDocument/' + aDocument.id ).success(function(response){
								 if(response.result){
									 aDoc.anEditDocument = response.document;
								 }
							}); 
			            }else {
			            	
			            }
			        	
			        	
			        }, function (resp) {
			            console.log('Error status: ' + resp.status);
			        }, function (evt) {
			            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
			            console.log('progress: ' + progressPercentage + '% ' );
			        });
			 }else {
				// alert(messages);
			 }
		 }
		 
		 
	 }]);
	
 
	 
 
})();
 
function movedElement(theElement){
	 console.log(theElement);
}

$('button[class*=btn-primary-outline]').click(function() {
    $(this).addClass('active').siblings().removeClass('active');

    // TODO: insert whatever you want to do with $(this) here
});