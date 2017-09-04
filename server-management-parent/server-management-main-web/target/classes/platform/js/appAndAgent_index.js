var grid;
var vue;

$(function(){
	
	vue = new Vue({
		el: '#app',
		data: function(){
			return {
				checkboxCols:[],//列表字段
				applications: null,//应用程序下拉数据
				agents:null,//代理应用程序下拉数据
				applicationVersions:null, //应用程序版本下拉数据
				dialogGridSelectionVisible: false,//列表字段选择dialog显示
				fullscreenLoading: false,//全屏loading条
				toolBarForm: {
					value: "",
					condition: ""
				},//top工具form数据
				form: {applictionAgent:null,appliction:null,applicationVersion:null},
		        formRules: {
		        	applictionAgent:[
					],
					appliction:[
					],
					applicationVersion:[
					]
		        },
		        dialogFormVisible: false,//dialog是否显示
		        formEdit: false,
		        submitBtnName: "立即创建"
			};
		},
		methods:{
			
			//初始化应用程序数据
			initApplicationData:function(){
				this.applications = this.getAllApplication();
				this.agents = this.getAllAgent();
			},
			
			//得到所用的应用程序数据
			getAllApplication:function(){
				var applicationData = [];
				PlatformUI.ajax({
					url: contextPath + "/appAndAgent/getAllApplication",
					async:false,
					afterOperation: function(data, textStatus,jqXHR){
						applicationData = data;
					}
				});
				return applicationData;
			},
			
			//得到所有的代理应用程序数据
			getAllAgent:function(){
				var agentData = [];
				PlatformUI.ajax({
					url: contextPath + "/appAndAgent/getAllAgent",
					async:false,
					afterOperation: function(data, textStatus,jqXHR){
						agentData = data;
					}
				});
				return agentData;
			},
			
			//得到所用的应用程序版本
			getAllAppVersion:function(applicationId){
				var appVersionData = [];
				PlatformUI.ajax({
					url: contextPath + "/appAndAgent/getAllAppVersion",
					data:{applicationId:applicationId},
					async:false,
					afterOperation: function(data, textStatus,jqXHR){
						appVersionData = data;
					}
				});
				return appVersionData;
			},
			
			//应用程序选中值时触发
			applicationHandler:function(value){
				applicationId = value;
				this.applicationVersions = this.getAllAppVersion(applicationId);
			},
			
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
				
				var rowData = grid.getRowData(ids);
				
				if(rowData['startStatus'] == 'starting' || rowData['startStatus'] == 'running'){
					Toast.success('程序已启动,若要编辑,请停止在编辑,或者刷新在看看!');
					return;
				}
				var context = this;
				this.dialogFormVisible = true;
				this.formEdit = true;
				this.submitBtnName = "编辑提交";
				PlatformUI.ajax({
					url: contextPath + "/appAndAgent/" + ids[0],
					afterOperation: function(data, textStatus,jqXHR){
						delete data.createDate;
//						data.startDate = new Date(data.startDate);

						var applicationId = data.appliction.id;
        				delete data["appliction"];
        				var applictionAgentId = data.applictionAgent.id;
        				delete data["applictionAgent"];
        				data = $.extend(data, {"appliction":applicationId});
        				data = $.extend(data, {"applictionAgent":applictionAgentId});
        				
        				context.form = $.extend(context.form, data);
					}
				});
			},
			deploy:function(){//部署
				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message: "选择一条要部署的数据的数据!", type: "warning"});
					return;
				}
				
				var rowData = grid.getRowData(ids);
				
				if(rowData['startStatus'] == 'starting' || rowData['startStatus'] == 'running'){
					Toast.success('程序已启动,若要部署,请停止在部署!');
					return;
				}else{
					Toast.success('程序程序正在部署,可能需要几分钟,请稍等...');
				}
				
				PlatformUI.ajax({
					url: contextPath + "/appAndAgent/deploy/" + ids[0],
					afterOperation: function(data, textStatus,jqXHR){
						//部署成功!
						PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
					}
				});
				
			},
			
			upgrade:function(){//升级
				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message: "选择一条要升级的数据的数据!", type: "warning"});
					return;
				}
				
				PlatformUI.ajax({
					url: contextPath + "/appAndAgent/upgrade/" + ids[0],
					afterOperation: function(data, textStatus,jqXHR){
						//升级成功!
						PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
					}
				});
				
			},
			
			startup:function(){//启动
				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message: "选择一条要启动的数据的数据!", type: "warning"});
					return;
				}
				
				PlatformUI.ajax({
					url: contextPath + "/appAndAgent/startup/" + ids[0],
					afterOperation: function(data, textStatus,jqXHR){
						//启动成功!
						PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
					}
				});
				
			},
			
			suspend:function(){//停止
				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message: "选择一条要停止的数据的数据!", type: "warning"});
					return;
				}
				
				PlatformUI.ajax({
					url: contextPath + "/appAndAgent/suspend/" + ids[0],
					afterOperation: function(data, textStatus,jqXHR){
						//停止成功!
						PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
					}
				});
				
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
						url: contextPath + "/appAndAgent",
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
				PlatformUI.exportGrid("list", "from base_app_agent");
			},
			resetForm: function(){
				this.dialogFormVisible = false;
				this.$refs['form'].resetFields();
				this.form = {applictionAgent:null,appliction:null,applicationVersion:null};
			},
			onSubmit: function(){//弹出表单的提交
				var context = this;
        		this.$refs['form'].validate(function(valid){
        			if (valid) {
        				var data = $.extend({}, context.form);
        				var applicationId = data.appliction;
        				delete data["appliction"];
        				var applictionAgentId = data.applictionAgent;
        				delete data["applictionAgent"];
        				data = $.extend(data, {"appliction.id":applicationId});
        				data = $.extend(data, {"applictionAgent.id":applictionAgentId});
//						data.startDate = ExtendDate.getFormatDateByLong(data.startDate.getTime(), 'yyyy-MM-dd');
        				var actionUrl = contextPath + "/appAndAgent";
        				if(context.formEdit){
        					actionUrl = contextPath + "/appAndAgent/" + data.id;
				            data['_method'] = "put";
        				}
        				PlatformUI.ajax({
			            	url: actionUrl,
			            	type: "post",
			            	dataType: 'json',
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
	
	//初始化数据
	initData();
	
	//绑定jqgrid resize事件
	$(window).bind('resize', function() {
		PlatformUI.resizeGridWidth(grid, 35);
	});
	
	grid = $("#list").jqGrid({
        url: contextPath + "/appAndAgent",
        datatype: "json",
        autowidth: true,
        height:300,
        mtype: "GET",
        multiselect: true,
        colNames: ['ID','应用程序','应用程序版本','应用程序PID','应用代理IP','启动日期','状态code','启动状态','创建时间'],
        colModel: [
			{ name: 'id', index:'id',hidden: true},
			{ name: 'appliction.name', index:'appliction.name', align:'center', sortable: true},
			{ name: 'applicationVersion', index:'applicationVersion', align:'center', sortable: true},
			{ name: 'applicationPid', index:'applicationPid', align:'center', sortable: true},
			{ name: 'applictionAgent.ip', index:'applictionAgent.ip', align:'center', sortable: true},
			{ name: 'startDate', index:'startDate',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }},
			{ name: 'startStatus', index:'startStatus', hidden: true},
			{ name: 'startStatusName', index:'startStatusName', align:'center', sortable: true},
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
	
	pollingRefresh();
			
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

/**
 * 初始化数据
 */
function initData(){
	vue.initApplicationData();//初始化应用程序数据
}

/**
 * 轮询刷新
 */
function pollingRefresh(){
	if(grid != null && grid != undefined){
		setInterval(function(){
			PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
		},1000*15);
	}
}
