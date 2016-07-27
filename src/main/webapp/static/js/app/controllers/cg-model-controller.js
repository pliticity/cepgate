function CgModelController(ctrl,modelService){

    ctrl.model = {};
    ctrl.modelForm = {};

    ctrl.saveModel = function(){
        if(modelService.saveModel(ctrl.modelForm,ctrl.model)==true){
            ctrl.model = modelService.getNew();
        };
    };

    ctrl.setModel = function(modelId){
        ctrl.model = modelService.getOne(modelId);
    };

    ctrl.newModel = function(){
        ctrl.model = modelService.getNew();
    };

};