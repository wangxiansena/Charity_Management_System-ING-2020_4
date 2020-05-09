<template>
   <div id="detail">
       <!-- 面包导航 -->
       <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
           <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
           <el-breadcrumb-item>其他管理</el-breadcrumb-item>
           <el-breadcrumb-item to="/rumors">谣言列表</el-breadcrumb-item>
           <el-breadcrumb-item>谣言详情</el-breadcrumb-item>
       </el-breadcrumb>
       <!--           详情部分-->
       <el-card style="min-height: 680px;">
           <div class="title">
               <h1 style="font-size: 25px;text-align: center;margin-top: 10px;" >{{this.detailInfo.title}}</h1>
           </div>
           <div class="source" style="font-size: 12px; color: #999;margin-bottom: 10px;">来源:<span>{{this.detailInfo.source}}</span></div>
           <div class="creatTime" style="font-size: 11px;color: #303030;margin-bottom: 5px;">
               时间 :{{detailInfo.createdAt}}
           </div>

           <div class="tag">
               标签:
               <el-tag size="mini" effect="plain" style="margin-left: 20px" v-for="tag in detailInfo.tag">
                   {{tag}}
               </el-tag >
               <div style="float: right;">

                   分类:<el-tag effect="plain" size="mini" type="success">{{detailInfo.category}}</el-tag>
               </div>
           </div>

           <div class="result" style="float: right;">
               <el-tag v-if="detailInfo.explain=='真'" size="mini" effect="dark">{{detailInfo.explain}}</el-tag>
               <el-tag v-else-if="detailInfo.explain=='假'" type="danger" size="mini" effect="dark">{{detailInfo.explain}}</el-tag>
               <el-tag v-else-if="detailInfo.explain=='疑'" type="warning" size="mini" effect="dark">{{detailInfo.explain}}</el-tag>
               <el-tag v-else type="info" siz size="mini" effect="dark">{{detailInfo.explain}}</el-tag>
           </div>
           <div  class="rumor" style="margin-top: 20px;font-size: 16px;color: #F56C6C;font-weight:500;margin-bottom: 10px;">
               {{this.detailInfo.rumor}}
           </div>
           <div class="content" style="color: #303030;font-size: 14px;">
               <p v-html="detailInfo.content"></p>
           </div>
           <div class="author">
               <el-avatar :size="100" :src="detailInfo.Author.head"></el-avatar>
               <span style="color: #5cb6ff;">{{detailInfo.Author.name}}</span>
               :
               <span style="color: #00ff5d;">{{detailInfo.authordesc}}</span>
           </div>

           <div class="copy-right" style="margin-top: 50px;color: #666;font-size: 12px;">
               {{detailInfo.copyright}}
           </div>
       </el-card>

   </div>

</template>
<script>
    export default {
        data() {
            return {
               id:'',
                detailInfo:{},
            }
        },
        methods:{
            //详情信息
            async detail(id){
                const { data: res } = await this.$http.get("rumor/detail/"+id);
                if (res.code !== 200) {
                    return this.$message.error("获取谣言列表失败");
                } else {
                    this.detailInfo=res.data;
                    this.detailInfo.content=this.unescape(this.detailInfo.content);

                }
            },
            //转义html
            unescape(html) {
                return html
                    .replace(html ? /&(?!#?\w+;)/g : /&/g, '&amp;')
                    .replace(/&lt;/g, "<")
                    .replace(/&gt;/g, ">")
                    .replace(/&quot;/g, "\"")
                    .replace(/&#39;/g, "\'");
            }
        },
        created() {
            this.id=this.$route.query.id;
            this.detail(this.id);
        }
    }

</script>

<style>
.content img{
    width: 100%;
    max-height: 700px;
}
</style>