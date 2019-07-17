package test;

import com.nfdw.Application;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ActivitiDemo2 {

    @Autowired
    private RepositoryService repositoryService;
    // 使用 RepositoryService 部署 流程 定义
    /*
        涉及到 的表
        act_re_deployment （部署对象表）  存放 流程定义的显示名 和 部署时间，每部署一次增加一条记录
        act_re_procdef （流程定义表） 存放流程定义的属性信息，部署每个新的流程定义都会在这张表中增加一条记录。
        act_ge_bytearry（资源文件表） 存储流程定义相关的部署信息。即流程定义文档的存放地。每部署一次就会增加两条记录，
     一条是关于bpmn规则文件的，一条是图片的
     */

    @Test
    public void deploy(){

        //获取流程引擎
      //  ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取仓库服务的实例
        Deployment deployment = repositoryService
                .createDeployment()
                .addClasspathResource("bpmn/leave.bpmn")
                .addClasspathResource("bpmn/Leave.png")
                .deploy();
        System.out.println(deployment.getId()+"   "+ deployment.getName());
    }

    // 使用RuntimeService 启动流程实例
    public void startProcess(){
        // 获取流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 启动流程
        // 使用流程定义的key启动流程实例，默认会按照最新版本启动流程实例
        ProcessInstance myProcess = processEngine.getRuntimeService().startProcessInstanceByKey("myProcess");
        System.out.println("pid:" + myProcess.getId()+",activitiId:"+myProcess.getActivityId());
    }

    //使用 taskService  查看任务
    @Test
    public void queryMyTask(){
        // 指定任务办理这
        String assignee = "acfc0e9232f54732a5d9ffe9071bf572";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 查询任务的列表
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        for(Task task : tasks){
            System.out.println("taskId:" + task.getId()+ ",taskName:" + task.getName());

        }
    }

    // 使用 taskService 完成我的个人任务
    @Test
    public void completeTask(){
        String taskId = "22509";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 完成任务
        processEngine.getTaskService()
                        .complete(taskId);
        System.out.println("完成任务.....");
    }

    /*
        查看 流程定义
        id : key:version:（随机值）
        name : 对应流程文件process 节点的name 属性
     */
    @Test
    public void queryProcessDefinition(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<ProcessDefinition> pdList = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc().list();


        for(ProcessDefinition pd : pdList){
            System.out.println("id:"+ pd.getId());
            System.out.println("name:"+pd.getName());
            System.out.println("key:"+pd.getKey());
            System.out.println("version:"+pd.getVersion());
            System.out.println("resourceName:"+pd.getDiagramResourceName());
        }
    }

    // 删除 部署到 activiti 的 流程定义
    @Test
    public void deleteDeployment(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String deploymentId = "27504";
        // 获取仓库服务对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //普通删除
        repositoryService.deleteDeployment(deploymentId,true);
    }

    // 查看 流程附件(查看流程图片)
    public void viewImage() throws Exception {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String deploymentId = "401";
        List<String> names = processEngine.getRepositoryService()
                .getDeploymentResourceNames(deploymentId);
        String imageName = null;
        for(String name:names){
            System.out.println("name:" + name);
            if(name.indexOf(".png")>=0){
                imageName = name;
            }
        }

        System.out.println("imageName:"+imageName);
        if(imageName!=null){
            File f = new File("e:/"+imageName);
            InputStream in = processEngine.getRepositoryService()
                    .getResourceAsStream(deploymentId, imageName);
            FileUtils.copyInputStreamToFile(in,f);
        }
    }

    /*
    查看最细版本的流程定义
     */
    public void queryAllLastestVersions(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 查询, 把最大的版本 都排到 后面
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc()
                .list();
        // 过滤出最新的版本
        Map<String,ProcessDefinition> map = new LinkedHashMap<String,ProcessDefinition>();
        for(ProcessDefinition pd:list){
            map.put(pd.getKey(),pd);
        }
        for(ProcessDefinition pd:map.values()){
            System.out.println("id:"+pd.getId());
        }
    }

    // 删除 流程定义(删除key相同的所有不同版本的流程定义)
    @Test
    public void deleteByKey(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 查询指定key的所有版本的流程定义
      /*  processEngine.getRepositoryService()
                .deleteDeployment("32501",true);*/
     /*   List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey("myProcess")
                .list();
        // 循环删除
        for(ProcessDefinition pd: list){
            processEngine.getRepositoryService()
                    .deleteDeployment(pd.getId(),true);
        }*/

       //  processEngine.getRepositoryService().deleteModel("37501");
        List<ProcessInstance> list = processEngine.getRuntimeService()
                .createProcessInstanceQuery().deploymentId("37504").list();
        for(ProcessInstance processInstance:list){
            System.out.println(processInstance.getName());
        }
        System.out.println("删除成功!");

    }

    // 设置流程 变量
    /*
    setVariable: 设置流程变量的时候，流程变量名称相同的时候，后一次的值替换前一次的值，
    而且可以看到task_id 的字段 不会存放任务ID的值

    setVariableLocal: 设置流程变量的时候，针对当前活动的节点设置流程变量，如果一个流程中
    存在2个活动节点，对每个活动节点都设置流程变量，即使流程变量的名称相同，后一次的版本
    的值也不会替换前一次版本的值，它会使用不同的任务ID作为标识，存放2个流程变量值，
    而且可以看到TASK_ID 的字段会存放任务ID的值

    还有，使用setVariableLocal 说明流程变量绑定了当前的任务，当流程继续执行时，下个任务
    获取不到这个流程变量(因为正在执行的流程变量中没有这个数据)，所以查询正在执行的任务时
    不能查询到我们需要的数据，此时需要查询历史的流程变量

     */
    public void setVariables(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        // 指定办理人
        String assigneeUser = "张三";
        // 流程实例 ID
        String processInstanceId = "1101";
        Task task = taskService.createTaskQuery().taskAssignee(assigneeUser)
                .processInstanceId(processInstanceId)
                .singleResult();
        // 变量 中存放 基本数据类型
        // 使用流程变量 的名称 和 流程变量的值 设置 流程 变量，一次只能设置一个值
        taskService.setVariable(task.getId(),"请假人","nfdw1");
        taskService.setVariableLocal(task.getId(),"请假天数",6);
        taskService.setVariable(task.getId(),"请假日期",new Date());
        // 变量中存放 javabean
        taskService.setVariable(task.getId(),"人员信息",new Object());
    }


    // 获取 流程 变量
    public void getVariables(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        // 指定 办理人
        String assigneeUser = "李四";
        // 流程实例 ID
        String processInstanceId = "1101";

        Task task = taskService.createTaskQuery() //创建任务查询
                .taskAssignee(assigneeUser)   // 指定办理人
                .processInstanceId(processInstanceId)  //指定流程实例ID
                .singleResult();
        // 变量中存放基本数据类型
        String stringValue = (String)taskService.getVariable(task.getId(),"请假人");
        Integer integerValue = (Integer)taskService.getVariableLocal(task.getId(), "请假天数");
        Date dateValue = (Date) taskService.getVariable(task.getId(),"请假日期");
        System.out.println(stringValue+"  "+ integerValue+ " "+dateValue);

        //javabean
        Object o = taskService.getVariable(task.getId(),"人员信息");

    }

   // 模拟流程变量的设置和获取的场景
    /*
    RuntimeService 对象 可以 设置 流程变量 和 获取 流程 变量
    TaskService 对象可以设置  流程变量和获取流程变量
    流程实例启动的时候可以设置流程变量
    任务办理完成的时候可以设置流程变量
    流程变量可以通过名称/值得形式设置当个流程变量
    流程变量可以通过Map集合，同时设置多个流程变量
    Map 集合 的  key 表示流程变量的名称
    Map 集合 的 value 表示流程变量的值

     */
    public void setAndGetvariables(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        TaskService taskService = processEngine.getTaskService();
        // 设置 变量的方法
        // 通过Execution 设置一个变量
        String executionId = "";
        //runtimeService.setVariable();

    }

    // 历史流程实例查看(查找按照某个规则一共执行了多少次流程)
    public void queryHistoricProcessInstance(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<HistoricProcessInstance> hpList = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processDefinitionKey("myProcess")
                .orderByProcessInstanceStartTime()
                .list();
        for(HistoricProcessInstance hpi : hpList){
            System.out.println("pid:"+ hpi.getId());
        }
    }

    // 历史任务查看(某一次流程的执行经历的多少任务节点)
    public void queryHistoricTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String processInstanceId = "1101";
        List<HistoricTaskInstance> htiList = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();
        for(HistoricTaskInstance hti : htiList){
            System.out.println("taskId :" + hti.getId());
        }

    }

    // 历史活动查看，某一次流程的执行一共经历了多少个活动
    public void queryHistoricActivityInstance(){
        String processInstanceId = "1101";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<HistoricActivityInstance> haiList = processEngine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();
        for(HistoricActivityInstance hai: haiList){
            System.out.println("activitiId:"+ hai.getActivityId());
        }
    }

    // 某一次流程的执行一共设置的流程变量
    public void queryHistoricVariables(){
        String processInstanceId = "2401";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<HistoricVariableInstance> hviList = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByVariableName().asc().list();

        if(hviList!=null && hviList.size()>0){
            for(HistoricVariableInstance hiv: hviList){
                System.out.println("pid:"+ hiv.getProcessInstanceId());
            }
        }

    }





    // 某一次流程的执行一共设置的流程变量

    // 查询 历史的 流程变量
    public void getHisVariables(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .variableName("请假天数")
                .list();
        if(list!=null && list.size()>0){
            for(HistoricVariableInstance hvi:list){
                System.out.println(hvi.getVariableName()+ "  "+ hvi.getValue());
            }
        }

    }


    // 查询我的个人任务
    public void findMyTaskList(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        /*processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId()*/

        String userId = "赵六";
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(userId) // 指定个人任务查询
                .list();
        for(Task task : list){
            System.out.println("id="+task.getId());
        }
    }


}




























































