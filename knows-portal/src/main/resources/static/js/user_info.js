let userApp=new Vue({
    el:"#userApp",
    data:{
        user:{}
    },
    methods:{
        loadUserVO:function(){
            axios({
                url:"/v1/users/me",
                method:"get"
            }).then(function(response){
                // 将控制器返回的UserVO对象,赋值给当前VUE声明的变量user
                // then方法中调用当前Vue对象的变量必须使用Vue对象名称
                // 不能使用this,因为这里的this指的是axios对象,不是Vue对象
                userApp.user=response.data;
            })
        }
    },
    created:function(){
        //页面加载完毕之后运行的方法
        // 一般要调用methods中的方法
        this.loadUserVO();
    }
})