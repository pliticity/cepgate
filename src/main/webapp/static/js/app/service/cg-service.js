function CgService(serv, resource){

    serv.getAll = function (params) {
        return resource.query(params);
    };

    serv.getOne = function (id) {
        return resource.get({id:id});
    };

    serv.save = function (form, data,callback) {
        form.$submitted = true;
        if (form.$valid) {
            resource.save({},data,callback);
            form.$submitted = false;
            return true;
        }else{
            return false;
        }
    };

    serv.getNew = function () {
        return resource.get({new:true});
    };

    serv.deleteOne = function(ids,callback){
      resource.delete({ids:ids},callback);
    };

};