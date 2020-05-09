<template>
    <div>
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>其他管理</el-breadcrumb-item>
            <el-breadcrumb-item>谣言列表</el-breadcrumb-item>
        </el-breadcrumb>
<!--        列表部分-->
        <el-card style="height: 850px;">
            <el-button-group>
                <el-button size="mini" type="primary"  @click="firstPage" :disabled="this.page==1">首页</el-button>
                <el-button size="mini" type="primary" icon="el-icon-arrow-left" @click="prePage" :disabled="this.page==1">上一页</el-button>
                <el-button size="mini" type="primary" @click="nextPage" :disabled="this.rumorList.length==0">下一页<i class="el-icon-arrow-right el-icon--right" ></i></el-button>
            </el-button-group>
            <el-row :gutter="20">
                <el-col style="margin-top: 10px;" v-for="item in rumorList" :span="12">
                    <div class="grid-content bg-purple">
                        <el-row>
                            <el-col :span="10">
                                <div class="grid-content bg-purple">
                                    <el-image :src="item.coversqual" style="height: 140px;width: 250px"></el-image>
                                </div>
                            </el-col>
                            <el-col :span="14" >
                                <div class="grid-content bg-purple-light">

                                        <h5 style="margin: 0px;;cursor: pointer;"  @click="detail(item.id)">{{item.title}}</h5>


                                    <div>
                                        <span style="font-size: 11px">时间:</span><span style="font-size: 11px;">{{item.date}}</span>
                                    </div>
                                    <div>
                                        <span style="font-size: 11px">描述:</span><span style="color: #999;font-size: 12px;">{{item.abstract}}</span>
                                    </div>
                                    <div>
                                        <span style="font-size: 11px">作者:</span><span style="font-size: 11px;color: #E6A23C;">{{item.author}}</span>
                                    </div>

                                    <el-row>
                                        <el-col :span="4"><div class="grid-content bg-purple">

                                            <div>
                                                <span style="font-size: 11px"> 结果:</span>
                                                <el-tag v-if="item.result=='真'" size="mini" effect="dark">{{item.result}}</el-tag>
                                                <el-tag v-else-if="item.result=='假'" type="danger" size="mini" effect="dark">{{item.result}}</el-tag>
                                                <el-tag v-else-if="item.result=='疑'" type="warning" size="mini" effect="dark">{{item.result}}</el-tag>
                                                <el-tag v-else type="info" size="mini" effect="dark">{{item.result}}</el-tag>
                                            </div>
                                        </div></el-col>
                                        <el-col :span="20"><div class="grid-content bg-purple-light"></div>
                                            <div style="margin-top: 10px;float: right">
                                                <span style="font-size: 12px">标签 :</span>
                                                <el-tag style="margin-left: 5px;" v-for="tag in item.tag" size="mini" effect="plain">
                                                    {{tag}}
                                                </el-tag>
                                            </div>
                                        </el-col>
                                    </el-row>

                                </div>
                            </el-col>
                        </el-row>
                    </div>
                </el-col>

            </el-row>
        </el-card>
    </div>
</template>

<script>
    export default {
        data () {
            return {
                count: 0,
                page:1,
                rumorList:[],
            }
        },
        methods: {
            //跳转到详情
            detail(id){

                this.$router.push({
                    path: '/rumors/detail',
                    query: {
                        id: id
                    },

                });
            },
            /**加载数据*/
            //加载系统日志列表
            async getData(page) {
                const { data: res } = await this.$http.get("rumor/list/"+page);
                if (res.code !== 200) {
                    return this.$message.error("获取谣言列表失败");
                } else {
                  this.rumorList=res.data;
                }
            },
            //下一页
            nextPage(){
                this.page=this.page+1;
                this.getData(this.page);
            },
            prePage(){
                this.page=this.page-1;
                this.getData(this.page);
            },
            firstPage(){
                this.page=1;
                this.getData(this.page);
            }
        },
        mounted() {
            //template挂载到页面时调用
            this.getData(1); //执行getData方法
        },
    }
</script>