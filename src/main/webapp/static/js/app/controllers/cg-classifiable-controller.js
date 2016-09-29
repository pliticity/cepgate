function CgClassifiableController(ctrl,classificationService,modelName){

    ctrl.classifications = [];

    ctrl.onClassificationSelected = function(){
        classificationService.onClassificationSelected(ctrl.classifications,ctrl.model.classification);
    };

    ctrl.getClassifications = function(){
        ctrl.classifications = classificationService.getAllFor(modelName);
    };

    ctrl.getClassifications();

};