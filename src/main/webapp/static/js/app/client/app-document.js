(function(){
	var documentApp = angular.module('document');
	documentApp.controller('DocumentController', ['$cookieStore','$cookies','$http', '$scope', 'Upload', '$compile', '$uibModal', function($cookieStore, $cookies,$http, $scope, Upload, $compile, $uibModal){
		console.log("DC")
		console.log($cookies.getAll());
		//console.log("DC - cookiesStore "+$cookieStore.getAll());
		var doc = this;
		 doc.recentDocuments = [];
		 doc.searchDocuments = [];
		 doc.myDocuments = [];
		 doc.favoriteDocuments = [];
		 doc.currentId  = "";
		 doc.newDocument = {};
		 doc.currentTab = "my";
		 doc.responseMessage = "";
		 $scope.newDocument = doc.newDocument;
		 doc.recentTable = null;
		 doc.recentDays = 20;
		 doc.searchTable = null;
		 doc.copyDocument = null;
		 $scope.myCurrentPage = 1;
		 $scope.myPageSize = 10;
		 
		 $scope.favoriteCurrentPage = 1;
		 $scope.favoritePageSize = 10;
		 
		 $scope.recentCurrentPage = 1;
		 $scope.recentPageSize = 10;
		 
		 
		 $scope.searchCurrentPage = 1;
		 $scope.searchPageSize = 10;
		 
		 doc.currentDownloadFiles = [];
		 $scope.editDocument = {};
		 $scope.createMessage = "";
		 $scope.saveMessage = "";
		 var saveMessageDiv = angular.element( document.querySelector( '#saveMessageDiv' ) ); 
		 var createMessageDiv = angular.element( document.querySelector( '#createMessageDiv' ) );
		
		 $http.get('/signup/authenticated').success(function(response){
			 if(response.result == "false"){
				 window.location = "/signup/login";
			 } 
		 }); 
		 
		 doc.shouldShowTab = function(theTab){
			 return doc.currentTab == theTab;
		 }
		
		 doc.showTab = function(theTab){
			 return doc.currentTab = theTab;
		 }
		
		 
		 
		 $scope.shouldShowMessage = function(theType){
			 if(theType == "save"){
				 return $scope.saveMessage.length > 0;
			 }else {
				 return $scope.createMessage.length > 0;
			 }
		 }
		 
		 $scope.selectedFile = function(aFile){ 
			 doc.newDocument.files.push(aFile);
			 console.log(doc.newDocument.files);
		 }
		 doc.isSet = false;
		 $scope.reloadDocuments = function(){
			 doc.reloadDocuments();
		 }
		 
		 doc.formatDocuments = function(documentList){
			 var tempDoc, parts, newClassification;
			 for(var i = 0; i < documentList.length; i++){
				 tempDoc = documentList[i];
				 newClassification = [];
				 if(tempDoc.tags && tempDoc.tags.length > 0){ 
					 parts = tempDoc.tags.split(",");
					 if(parts){
						 for(var j = 0; j < parts.length; j++){
							 if(parts[j].length > 0){
								 newClassification.push({text: parts[j], drag: true});
							 }
						 }
					 }					 
					 tempDoc.tags = newClassification;
				 }else {
					 tempDoc.tags = [];
				 }
				 documentList[i] = tempDoc;
			 }
			 return documentList;
		 }
		 
		 doc.getClassification = function(classification){
			 var toReturn = "";
			 if(classification && classification.length > 0){
				 toReturn = classification[0].text;
			 }
			 return toReturn;
		 }
		 doc.reloadDocuments = function(){ 
			 $http.get('/documents/loadDocuments?type=all&days=' + doc.recentDays).success(function(response){
				 if(!response || response.length == 0){
					 doc.myDocuments = [];
					 doc.recentDocuments = [];
					 doc.searchDocuments = [];
					 doc.favoriteDocuments = [];
				 }else {
					  
					 doc.recentDocuments = doc.formatDocuments(response.recent_documents);
					 doc.myDocuments = doc.formatDocuments(response.my_documents);
					 doc.searchDocuments = doc.formatDocuments(response.search_documents);
					 doc.favoriteDocuments = doc.formatDocuments(response.favorite_documents);
					 
				 } 
				$("#recentTable").css("width", "100%");
				$("#myTable").css("width", "100%");
				$("#favoriteTable").css("width", "100%");
				$("#searchTable").css("width", "100%");
				
			 });
			  
			 doc.newDocument = doc.getNewDocument();
			 $scope.newDocument = doc.newDocument;
		 }
		 
		 $scope.editFileSelected = function(aFile){
			 for(var i = 0; i < aFile.files.length; i++){
				 
			 } 
		 } 
		 
		 doc.getNewDocument = function(){
			 var aDoc = {"caseId": "", "caseName": "", "documentName": "", "documentName2": "", "documentName3": "", 
					 "masterDocumentNo": "", "documentNumber": "", "internalFileName": "",
					 "documentNo": "", "documentType": "", "internalExternal": "" };
			 var files = [doc.getNewFile()];
			 aDoc["files"] = files;
			 $http.get('/documents/new/').success(function(response){
				 doc.newDocument.masterDocumentNo = padDigits(response.masterDocumentNo, 8);
				 doc.newDocument.documentNumber = padDigits(response.documentNumber, 8);
				 $scope.newDocument = doc.newDocument;
			 });
			 
			 return aDoc;
		 }
		 
		 doc.getNewFile = function(){
			 var aFile = {"fileName": ""};
			 return aFile;
		 }
		 
		 doc.hasResponseMessage = function(){
			 return doc.responseMessage.length > 0;
		 } 
		 
		 doc.downloadFile = function(aFile) {
			 $http.get('/documents/canDownload/' + aFile.documentId ).success(function(response){
				 if(response.can_download == "true"){
					window.location = "/documents/download/" + aFile.id;	
				}else {
					alert(response.description);
				}
			});
		 } 
		 
		 doc.downloadDocument = function(aDocument) {
			 $http.get('/documents/canDownload/' + aDocument.id).success(function(response){
				 if(aDocument.files.length > 0 && response.can_download == "true"){
					 var allIds = [];
					 
					 for(var i = 0; i < doc.currentDownloadFiles.length ; i++){
						 allIds.push(JSON.parse(doc.currentDownloadFiles[i]).documentId);
						  
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
		 doc.exportDocument = function(aDocument) {
			 $http.get('/documents/canDownload/' + aDocument.id ).success(function(response){
				console.log(response);
				 if(response.can_download == "true"){
					window.location = "/documents/export/" + response.id;	
				}else {
					alert(response.description);
				}
			});
		 }
		 
		 $scope.newDocument = function() {
			 $http.get('/documents/canDownload/' + aFile.documentId ).success(function(response){
				console.log(response);
				 if(response.can_download == "true"){
					window.location = "/documents/download/" + aFile.id;	
				}else {
					alert(response.description);
				}
			});
		 
			doc.newDocument = doc.getNewDocument();
			$scope.newDocument = doc.newDocument; 
		 }
		 
		 doc.removeFromArray = function(theArray, removeThis){
			 var toReturn = [];
			 for(var i = 0; i < theArray.length; i++){
				 if(theArray[i] != removeThis){
					 toReturn.push(theArray[i]);
				 }
			 }
			 return toReturn;
		 }
		 
		 doc.removeFile = function(actionType, aFile){
			 if(actionType == "edit"){
				 doc.editDocument.files = doc.removeFromArray(doc.editDocument.files, aFile);
			 }else {
				 doc.newDocument.files = doc.removeFromArray(doc.newDocument.files, aFile);
			 }
		 }
		 
		 $scope.addDocument = function() {
			 if(!doc.newDocument.files || doc.newDocument.files.length == 0){
				 var files = [doc.getNewFile()];
				 doc.newDocument.files = files;
			 } else {
				 doc.newDocument.files.push(doc.getNewFile());
				 
			 }
			 $scope.newDocument = doc.newDocument; 
		 } 
		 
		 $scope.editAddDocument = function() {
			 if(!doc.editDocument.files || doc.editDocument.files.length == 0){
				 var files = [doc.getNewFile()];
				 doc.editDocument.files = files;
			 }else {
				 doc.editDocument.files.push(doc.getNewFile());
				 
			 }
			  
			 doc.editDocument.masterDocumentNo = padDigits(doc.editDocument.masterDocumentNo, 8);
			 $scope.editDocument = doc.editDocument; 
		 } 
		 
		 doc.toggleFavorite = function(aDocument){
			 $http.get('/documents/markAsFavorite/' + aDocument.id ).success(function(response){
				 doc.reloadDocuments();
			}); 
		 }
		 doc.editUrl = "edit_document.html";
		 
		 doc.initiateDocumentCopy = function(aDocument){
			 doc.copyDocument = aDocument;
			 
			 doc.copyModalInstance = $uibModal.open({
			      animation: $scope.animationsEnabled,
			      templateUrl: 'copy_document.html', 
			      controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
			    	  $scope.files = doc.copyDocument.files;
			    	  $scope.copyFiles = [];
			    	  $scope.ok = function () {
			    		   
			    			  doc.doCopy($scope.copyFiles);
				    		  $uibModalInstance.close();
			    		 
			  		  };

			  		  $scope.cancel = function () {
			  		    $uibModalInstance.dismiss('cancel');
			  		  };
			      }],
			      resolve: {
			    	  files: function () {
			          return doc.copyDocument.files;
			        }
			      }
			    });			 
		 }
		 doc.doCopy = function (copyFiles) {
			 console.log(copyFiles);
			 $http({
				  method: 'POST',
				  url: '/documents/copyDocument' ,
				  data: {copyFiles : copyFiles, id: doc.copyDocument.id}
			 	}
			).success(function(response){
				doc.reloadDocuments();
				alert(response.description);
				
			}); 
		 };

		 doc.cancelCopy = function() {
			 console.log("here");
			 doc.copyModalInstance.dismiss('cancel');
		 };
			  
		 doc.edit = function(aDocument){
			 if(!aDocument.files || aDocument.files.length == 0){
				 var files = [doc.getNewFile()];
				 aDocument.files = files;
			 }
			 $http.get('/documents/markAsOpened/' + aDocument.id ).success(function(response){
				 doc.reloadDocuments();
			}); 
			 $('div.active').removeClass('active').removeClass('in');
			 $('li.active').removeClass('active');
			 if(document.getElementById('edit_' + aDocument.documentNumber ) == null){
				
				 aDocument.plannedIssueDate = new Date(aDocument.plannedIssueDate);
				 doc.editDocument = aDocument;
				 doc.editDocument.masterDocumentNo = padDigits(doc.editDocument.masterDocumentNo, 8);
				 $scope.editDocument = doc.editDocument; 
				
				 $('#docmentTabs').append(' <li role="presentation"><a href="#edit_' + aDocument.documentNumber + '" id="link_' + aDocument.documentNumber + '" aria-controls="new" role="tab" data-toggle="tab">' + aDocument.documentNumber + '</a></li>');
				 var html = '<div role="tabpanel" class="tab-pane" id="edit_' + aDocument.documentNumber + '" >' + 
				 	'<form  id="documentEditForm" name="documentEditForm' + aDocument.documentNumber + '" ng-controller="ADocumentController as aDocumentCtrl"  class="form-horizontal" method="POST"><div ng-include="\'edit_document.html\'"></div></form></div>';
				 var editTemplate = angular.element(document.getElementById('documentsTabContent'));
				 //console.log(editTemplate.context.innerHTML);
				 doc.currentId = aDocument.documentNumber ;
				 html = $compile(html)($scope); 
				 editTemplate.append(html); 
	             $('#edit_' + aDocument.documentNumber).tab('show');   
	             //$('#edit_' + aDocument.documentNumber).css('display', "block");  
	             document.getElementById('link_' + aDocument.documentNumber).click();
	            
	           //  $('input[name="tags"]').tagsInput();
			 }else {
				 $('#link_' + aDocument.documentNumber).tab('show');
			 }
		 }
		 
		 $scope.closeTab = function(aDocument){
		     $("#edit_container_link").css("display", "none"); 
		     $('#home_link').click(); 
		 }
		 
		 doc.deleteDocument = function(aDocument){
			 if(confirm("Are you sure you would like to delete the document?")){
				 $http.get('/documents/delete/' + aDocument.id ).success(function(response){
					 alert(response.description);
					 doc.reloadDocuments();
				}); 
			 } 
		 }
		 
		 
		 doc.cancel = function(){
			 doc.reloadDocuments();
		 }
		 
		 doc.saveDocuments = function(theType){
			 
			 var messages = "";
			 var aDoc = doc.newDocument;
			 var isGood = $scope.documentCreateForm.$valid; 
			 
			 if(theType == "save"){
				 aDoc = doc.editDocument;
				 isGood = $scope.documentEditForm.$valid;
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
			 
			 if(isGood && aDoc.documentName && aDoc.documentName.length > 300){
				 messages += "Document Name is too long. Please limit it to 300 characters\n";
				 isGood = false;
			 } 
			 
			 var theSaveItem = aDoc;
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
				 
				 Upload.upload({
			            url: theURL,
			            data:  theSaveItem
			        }).then(function (resp) {
			        	alert(resp.data.description);
			        	console.log('Success ' + 'uploaded. Response: ' + resp.data.description);
			            doc.reloadDocuments();
			           // window.location.reload();
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
		 
		 doc.reloadDocuments();
	 }]);
	
	
	 function DataReloadWithAjaxCtrl(DTOptionsBuilder, DTColumnBuilder) {
		    
		 documentApp.recentDtOptions = DTOptionsBuilder.fromSource('/documents/loadDocuments?type=recent');
		 documentApp.searchDtOptions = DTOptionsBuilder.fromSource('/documents/loadDocuments?type=recent');
		    
		 documentApp.recentDtColumns = [
	        DTColumnBuilder.newColumn('id').withTitle('ID'),
	        DTColumnBuilder.newColumn('firstName').withTitle('First name'),
	        DTColumnBuilder.newColumn('lastName').withTitle('Last name').notVisible()
	    ];
		 documentApp.searchDtColumns = [
	        DTColumnBuilder.newColumn('id').withTitle('ID'),
	        DTColumnBuilder.newColumn('firstName').withTitle('First name'),
	        DTColumnBuilder.newColumn('lastName').withTitle('Last name').notVisible()
	    ];
		                		        
		 documentApp.reloadData = reloadData;
		 documentApp.recentDtInstance = {};
		 documentApp.searchDtInstance = {};
		 $("#searchTable").css("width", "100%");
		$("#recentTable").css("width", "100%");
		
	    function reloadData() {
	        var resetPaging = false;
	        documentApp.recentDtInstance.reloadData(callback, resetPaging);
	        documentApp.searchDtInstance.reloadData(callback, resetPaging);
	    }

	    function callback(json) {
	        console.log(json);
	    }
	    reloadData();
	}
	 
	
	 documentApp.config(['$compileProvider',
         function ($compileProvider) {
             $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|tel|file|blob):/);
     }]);
	 
	 
	 $('#add-contact').click(function(e) { 
		 e.preventDefault();
	     var id = $(".nav-tabs").children().length; //think about it ;)
	     $(this).closest('li').before('<li><a href="#edit_'+id+'">Edit Document</a><span>x</span></li>');         
	     $('.tab-content').append('<div class="tab-pane" id="edit_'+id+'">'+ $("#document-edit").html() + '</div>');
	});
})();
 

$(".nav-pills").on("click", "a", function(e){
    e.preventDefault();
    $(this).tab('show');
  })
  .on("click", "span", function () {
      var anchor = $(this).siblings('a');
      $(anchor.attr('href')).remove();
      $(this).parent().remove();
      $(".nav-tabs li").children('a').first().click();
      $('#home').click();
  });

function renderMultiSelect(){
}
function padDigits(number, digits) {
    return Array(Math.max(digits - String(number).length + 1, 0)).join(0) + number;
}