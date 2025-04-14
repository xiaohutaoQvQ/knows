let questionApp = new Vue({
    el: "#questionApp",
    data: {
        question: {}
    },
    methods: {
        loadQuestion:function(){
            // question/detail_teacher.html?149
            // 我们需要?之后的id值才能调用axios查询问题详情
            let qid=location.search;
            // location.search可以获得地址栏中?之后的内容,具体规则如下
            // 1.如果地址栏中没有?   qid:""
            // 2.如果地址栏中有?但是?之后没有任何内容   qid:""
            // 3.如果地址栏中有?并且?之后有内容(例如149) qid:"?149"
            // 当路径question/detail_teacher.html?149  qid:?149
            // 我们先判断qid是否有值
            if(!qid){
                // 如果qid没有值,表示地址栏没有?或?后没有值,无法查询问题详情
                alert("请指定问题的id!");
                return;
            }
            // 如果qid有值,需要去掉?
            //  ?149  ->  149
            qid=qid.substring(1);
            axios({
                url:"/v1/questions/"+qid,
                method:"get"
            }).then(function(response){
                questionApp.question=response.data;
                addDuration(response.data);
            })
        }
    },
    created: function () {
        this.loadQuestion();
    }
})

// 下面是讲师回复表单提交的js代码
let postAnswerApp=new Vue({
    el:"#postAnswerApp",
    data:{},
    methods: {
        postAnswer:function(){
            // 我们提交表单需要当问题id和讲师回复的内容
            // 问题id在浏览器地址栏?后面,讲师回复的内容在富文本编辑器中
            // 本次提交并没有使用Vue来获取,而是我们自己编写代码获得
            let qid=location.search;
            if(!qid){
                return;
            }
            qid=qid.substring(1);
            // 使用jQuery代码获得讲师在富文本编辑器中编写的内容
            let content=$("#summernote").val();
            // 创建表单
            let form=new FormData();
            form.append("questionId",qid);
            form.append("content",content);
            axios({
                url:"/v1/answers",
                method: "post",
                data:form
            }).then(function(response){
                console.log(response.data);
                // response.data就是新增成功的answer对象
                let answer=response.data;
                answer.duration="刚刚";
                //将新增成功的回答添加到回答列表中
                answersApp.answers.push(answer);
                // 利用summernote给定的方法清空(重置)其中内容
                $("#summernote").summernote("reset");
            })
        }
    }
})


let answersApp=new Vue({
    el:"#answersApp",
    data:{
        answers:[]
    },
    methods:{
        loadAnswers:function (){
            // 也是需要从浏览器地址栏?之后获得问题id的
            let qid=location.search;
            if(!qid){
                return;
            }
            qid=qid.substring(1);
            axios({
                url:"/v1/answers/question/"+qid,
                method:"get"
            }).then(function(response){
                answersApp.answers=response.data;
                answersApp.updateDuration();
            })
        },
        updateDuration:function(){
            let answers=this.answers;
            for(let i=0;i<answers.length;i++){
                addDuration(answers[i]);
            }
        },
        postComment:function(answerId){
            // 新增评论功能,需要answerId和content
            // 参数就是answerId,我们需要获得的只有用户输入的内容
            // 用户输入的内容在一个textarea标签中
            // 我们使用jQuery的后代选择器选中这个标签
            //        这个空格千万别删               ↓↓
            let textarea=$("#addComment"+answerId+" textarea");
            let content=textarea.val();
            // 创建表单
            let form=new FormData();
            form.append("answerId",answerId);
            form.append("content",content);
            axios({
                url:"/v1/comments",
                method:"post",
                data:form
            }).then(function(response){
                //  将新增成功的评论显示在页面中
                let comment=response.data;
                //  获得当前所有回答的集合
                let answers=answersApp.answers;
                // 遍历所有回答的集合
                for(let answer of answers){
                    // 判断当前回答是不是新增评论的回答
                    if(answer.id==answerId){
                        // 如果确认一致,就将当前新增成功的评论对象
                        // 添加到这个回答的评论列表中
                        answer.comments.push(comment);
                        // 清空输入框的内容
                        textarea.val("");
                        break;
                    }
                }
            })
        },
        removeComment:function(commentId , index , comments){
            axios({
                url:"/v1/comments/"+commentId+"/delete",
                method:"get"
            }).then(function(response){
                if(response.data!="ok"){
                    alert(response.data);
                    return;
                }
                // 如果返回的response.data是"ok",才能运行下面代码
                // 利用js代码从数组中删除元素的api删除元素即可
                // splice实现删除数组中的元素,参数([删除下标的起始值],[删除的个数])
                comments.splice(index,1);
            })
        },
        updateComment:function(commentId,index,answer){
            // 获得用户修改后的评论内容,还使用jQuery的后代选择器
            let textarea=$("#editComment"+commentId+" textarea");
            let content=textarea.val();
            //  判断内容是否为空,如果是空终止修改
            if(!content){
                return;
            }
            // 为了启动服务器端的SpringValidation验证
            // 我们仍然提交CommentVO对象,answerId要传值,但无实际作用
            let form=new FormData();
            form.append("answerId",answer.id);
            form.append("content",content);
            axios({
                url:"/v1/comments/"+commentId+"/update",
                method:"post",
                data:form
            }).then(function (response){
                // 输出返回值的类型
                console.log(response.data);
                console.log("返回值类型:"+typeof(response.data))
                // 判断返回值类型是不是object
                if(typeof(response.data)=="object"){
                    // 如果返回值类型是object就是修改完成了!
                    // 本次修改没有变化数组元素的数量(数据长度没变)
                    // 因为当前要修改的数组元素已经是answers数组的子元素
                    // Vue是不会对这样的修改进行页面上内容的更新的
                    // 既然它不会自动修改,就需要我们手动修改
                    // Vue提供了一个方法支持我们手动修改数组元素
                    // 这个方法会引起页面信息的同步变化
                    // Vue.set([要修改的数组],[要修改的下标],[修改成什么])
                    Vue.set(answer.comments,index,response.data);
                    // 修改成功之后,编辑框设计为自动收缩
                    $("#editComment"+commentId).collapse("hide");
                }else{
                    alert(response.data);
                }
            })

        },
        answerSolved:function (answerId) {
            axios({
                url:"/v1/answers/"+answerId+"/solved",
                method:"get"
            }).then(function (response){
                alert(response.data);
            })
        }
    },
    created:function(){
        this.loadAnswers();

    }
})




