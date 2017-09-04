var grid;
var vue;

$(function(){
	
	vue = new Vue({
		el: '#app',
		data: function(){
			return {
				checkboxCols:[],//列表字段
				uploadBusinessAction:'',
				serverTypeList:null,//服务类型列表数据
				dialogGridSelectionVisible: false,//列表字段选择dialog显示
				dialogUploadVisible:false,//上传窗口dialog显示
				zipFileList:[],
				fullscreenLoading: false,//全屏loading条
				toolBarForm: {
					value: "",
					condition: ""
				},//top工具form数据
				form: {context:null,serverType:null,name:null},
		        formRules: {
					context:[
					],
					serverType:[
					],
					serverTypeName:[
					],
					hasJar:[
					],
					name:[
						{ required: '', message: '应用名称必填', trigger: 'change' }
					]

		        },
		        dialogFormVisible: false,//dialog是否显示
		        formEdit: false,
		        submitBtnName: "立即创建"
			};
		},
		methods:{
			compositeSearch: function(){//检索
				var context = this;
				grid.jqGrid("setGridParam", {
					postData: {filters:{}}
				});
				grid.jqGrid('searchGrid', {multipleSearch:true,drag:false,searchOnEnter:true,
					onSearch: function(){
						FieldtypeAddtionerFactory.create(grid).search();
						context.toolBarForm.value = '';
						context.toolBarForm.condition = '';
					}
				});
			},
			
			//初始化数据
			initData:function(){
				this.serverTypeList = this.getServerType();
			},
			
			//得到服务类型数据
			getServerType:function(){
				var serverTypeData = [];
				PlatformUI.ajax({
					url: contextPath + "/application/getServerType",
					async:false,
					afterOperation: function(data, textStatus,jqXHR){
						serverTypeData = data;
					}
				});
				return serverTypeData;
			},
			
			refreshPage: function(){//刷新
				this.fullscreenLoading = true;
				setTimeout(function(){
					location.reload();
				}, 1000);
			},
			add: function(){//新增
				this.dialogFormVisible = true;
				this.formEdit = false;
				this.submitBtnName = "立即创建";
			},
			edit: function(){//编辑
				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message: "选择一条要编辑的数据!", type: "warning"});
					return;
				}
				var context = this;
				this.dialogFormVisible = true;
				this.formEdit = true;
				this.submitBtnName = "编辑提交";
				PlatformUI.ajax({
					url: contextPath + "/application/" + ids[0],
					afterOperation: function(data, textStatus,jqXHR){
						delete data.createDate;
						data.createDate = new Date(data.createDate);

						context.form = $.extend(context.form, data);
					}
				});
			},
			upload:function(){//上传
				var id = grid.jqGrid ('getGridParam', 'selarrrow');
				if(id.length ==1){
					 this.dialogUploadVisible = true;
					 var action =  contextPath + "/application/upload/" + id;
					 this.uploadBusinessAction = action;
				}else if(id.length ==0){
					PlatformUI.message({message:"上传 , 请选择一条数据!", type:"warning"});
					return;
				}else{
					PlatformUI.message({message:"上传 , 有且只能选择一条数据!", type:"warning"});
					return;
				}
			},
			
		    uploadZipSuccess:function(data){
		    	this.zipFileList=[];
		    	if(data=="error"){
		    		PlatformUI.message({message: "上传失败，服务器未能成功接收文件，请重新上传!", type: "error"});
		    	}else{
		    		PlatformUI.message({message: "上传成功!", type: "success"});
		    		this.dialogUploadVisible = false;
		    		PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
		    	}
		    },
		    
		    deploy: function(){//部署
				window.open(contextPath+"/appAndAgent/index","_blank");
			},
		      
			del: function(){//删除 
				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length == 0){
					PlatformUI.message({message:"请至少选择一条要删除的数据!", type:"warning"});
					return;
				}
				this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
		          confirmButtonText: '确定',
		          cancelButtonText: '取消',
		          type: 'warning'
		        }).then(function(){
		        	PlatformUI.ajax({
						url: contextPath + "/application",
						type: "post",
						data: {_method:"delete",ids:ids},
						message:PlatformUI.message,
						afterOperation: function(){
							PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
						}
					});
		        });
			},
			exp: function(){//导出
				PlatformUI.exportGrid("list", "from base_application");
			},
			resetForm: function(){
				this.dialogFormVisible = false;
				this.$refs['form'].resetFields();
				this.form = {context:null,serverType:null,serverTypeName:null,hasJar:false,name:null};
			},
			onSubmit: function(){//弹出表单的提交
				var context = this;
        		this.$refs['form'].validate(function(valid){
        			if (valid) {
        				var data = $.extend({}, context.form);
//						data.createDate = ExtendDate.getFormatDateByLong(data.createDate.getTime(), 'yyyy-MM-dd');
        				var actionUrl = contextPath + "/application";
        				if(context.formEdit){
        					actionUrl = contextPath + "/application/" + data.id;
				            data['_method'] = "put";
        				}
        				PlatformUI.ajax({
			            	url: actionUrl,
			            	type: "post",
			            	data: data,
			            	message:PlatformUI.message,
			            	afterOperation: function(){
			            		context.toolBarForm.value = "";
			            		context.toolBarForm.condition = "";
			            		PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
			            	}
			            });
			            context.dialogFormVisible = false;
						context.$refs['form'].resetFields();
			        } else {
			            PlatformUI.message({message:"表单验证失败", type:"error"});
			            return false;
			        }
        		});
     		},
		    commonSearch: function(value){
		    	commonSearch();
		    },
		    selectGridColumn: function(){
		    	this.dialogGridSelectionVisible = true;
		    },
		    saveColVisible: function(){
		    	for(var i = 0; i < this.checkboxCols.length; i++){
		    		if(this.checkboxCols[i].visible){
		    			grid.showCol(this.checkboxCols[i].value);
		    		}else{
		    			grid.hideCol(this.checkboxCols[i].value);
		    		}
		    	}
		    	this.dialogGridSelectionVisible = false;
		    	//重设jqrid宽度
		    	PlatformUI.fineTuneGridSize(grid, 35);
		    }
		}
	});
	
	vue.initData();//初始化数据
	
	//绑定jqgrid resize事件
	$(window).bind('resize', function() {
		PlatformUI.resizeGridWidth(grid, 35);
	});
	
	grid = $("#list").jqGrid({
        url: contextPath + "/application",
        datatype: "json",
        autowidth: true,
        height:300,
        mtype: "GET",
        multiselect: true,
        colNames: ['ID','应用名称','应用上下文','服务类型','是否有jar','创建时间'],
        colModel: [
			{ name: 'id', index:'id',hidden: true},
			{ name: 'name', index:'name', align:'center', sortable: true},
			{ name: 'context', index:'context', align:'center', sortable: true},
			{ name: 'serverTypeName', index:'serverTypeName', align:'center', sortable: true},
			{ name:'hasJar', index:'hasJar',expType:'boolean',expValue:{'true':'是','false':'否'},align:'center', formatter: PlatformUI.defaultYNFormatter, stype:'select', searchoptions:{value:'true:是;false:否'}},
			{ name: 'createDate', index:'createDate',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }}
        ],
        pager: "#pager",
        rowNum: 10,
        rowList: [10, 20, 30],
        sortname: "createDate",
        sortorder: "desc",
        viewrecords: true,
        gridview: true,
        autoencode: true,
        caption: "列表",
    	gridComplete: function(){
    		PlatformUI.fineTuneGridSize(grid, 42);
    		//设置隐藏/显示列字段
    		vue.checkboxCols = [];
    		var gridColModel = grid.getGridParam("colModel");
	    	var gridColNames = grid.getGridParam("colNames");
	    	for(var i=0; i < gridColNames.length; i++){
	    		if(gridColNames[i].indexOf("role='checkbox'") == -1){
		    		var name = gridColNames[i];
					var value = gridColModel[i].name;
					var visible = true;
					if(gridColModel[i].hidden){
						visible = false;
					}
		    		vue.checkboxCols.push({name:name, value:value, visible:visible});
	    		}
	    	}
    	}
    });
			
});

/***********************方法区***************************/

function commonSearch(){
	var name = vue.toolBarForm.condition;
	var value = vue.toolBarForm.value;
	if(name == ""){
		PlatformUI.message({message:"请选择搜索条件", type:"warning"});
		return;
	}
	if(value == ""){
		PlatformUI.message({message:"请输入搜索内容", type:"warning"});
		return;
	}
	var rules = [{"field":name,"op":"cn","data":value.trim()}];
	var filters = {"groupOp":"AND","rules":rules};
	grid.jqGrid("setGridParam", {
		postData: {filters:JSON.stringify(filters)},
		page: 1
	}).trigger("reloadGrid");
}

