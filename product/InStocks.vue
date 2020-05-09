<template>
    <div id="inStocks">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>物资管理</el-breadcrumb-item>
            <el-breadcrumb-item>物资入库</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 卡片区域 -->
        <el-card>
            <!-- 搜索部分 -->
            <el-form :inline="true" :model="queryMap" class="demo-form-inline">
                <el-form-item label="类型">
                    <el-select @change="search" clearable @clear="search" v-model="queryMap.type" placeholder="请选择入库类型">
                        <el-option label="捐赠" value="1"></el-option>
                        <el-option label="下拨" value="2"></el-option>
                        <el-option label="采购" value="3"></el-option>
                        <el-option label="借用" value="4"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="单号">

                    <el-input
                            clearable
                            @clear="search"
                            v-model="queryMap.inNum"
                            placeholder="请输入入库单查询"
                            @keyup.enter.native="search"
                            class="input-with-select"
                    >
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="queryMap.status" @click="search" placeholder="请选择状态">
                        <el-option label="已入库" :value="0"></el-option>
                        <el-option label="回收站" :value="1"></el-option>
                        <el-option label="待审核" :value="2"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <router-link to="/inStocks/addStocks">
                        <el-button type="success" icon="el-icon-plus">入库</el-button>
                    </router-link>
                </el-form-item>
                <el-form-item>
                    <el-button type="button" icon="el-icon-download">导出</el-button>
                </el-form-item>

            </el-form>
            <!-- 表格区域 -->
            <el-table v-loading="loading" :data="tableData" border style="width: 100%;height:460px;">
                <el-table-column label="#" prop="id" width="50"></el-table-column>
                <el-table-column prop="inNum" label="入库单号" width="180"></el-table-column>
                <el-table-column prop="supplier" label="物资类型" width="100">
                    <template slot-scope="scope">
                        <el-tag size="mini" effect="dark" type="success" v-if="scope.row.type===1">捐赠</el-tag>
                        <el-tag size="mini" effect="dark" v-else-if="scope.row.type===2">下拨</el-tag>
                        <el-tag size="mini" effect="dark" type="danger" v-else-if="scope.row.type===3">采购</el-tag>
                        <el-tag size="mini" effect="dark" type="warning" v-else>借用</el-tag>
                    </template>
                </el-table-column>

                <el-table-column prop="productNumber" label="总数"  width="70"></el-table-column>
                <el-table-column prop="phone" label="联系方式" width="150"></el-table-column>
                <el-table-column prop="createTime" label="入库时间" width="180"></el-table-column>
                <el-table-column prop="supplierName" label="物资提供方" width="180"></el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                    <template slot-scope="scope">
                        <el-tag size="mini" type="danger" effect="plain" v-if="scope.row.status==1">回收</el-tag>
                        <el-tag size="mini" effect="plain" v-if="scope.row.status==0">已入库</el-tag>
                        <el-tag size="mini" effect="plain" type="warning" v-if="scope.row.status==2">审核中</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="operator" label="操作员" width="180"></el-table-column>
                <el-table-column label="操作" fixed="right" width="200">
                    <template slot-scope="scope">
                        <el-button icon="el-icon-view"  @click="detail(scope.row.id)"
                                   type="text" size="small">明细

                        </el-button>

                        <!--                        给操作员使用的按钮-->
                        <span v-if="scope.row.status==0">
                             <el-popconfirm
                                     @onConfirm="remove(scope.row.id)"
                                     style="margin-left: 20px;"
                                     confirmButtonText='好的'
                                     cancelButtonText='不用了'
                                     icon="el-icon-info"
                                     iconColor="red"
                                     title="这是一段内容确定移入回收站吗？"
                             >
              <el-button type="text" slot="reference" size="mini" icon="el-icon-s-operation">回收站</el-button>
            </el-popconfirm>
                        </span>
                        <!--   给操作员使用的按钮(回收站)-->
                        <span v-if="scope.row.status==1">
                             <el-popconfirm
                                     @onConfirm="back(scope.row.id)"
                                     style="margin-left:10px;"
                                     confirmButtonText='好的'
                                     cancelButtonText='不用了'
                                     icon="el-icon-info"
                                     iconColor="green"
                                     title="这是一段内容确定恢复数据吗？"
                             >
                            <el-button type="text" slot="reference" size="mini" icon="el-icon-s-operation">还原</el-button>
                        </el-popconfirm>
                                <el-popconfirm
                                        style="margin-left:20px;"
                                        @onConfirm="del(scope.row.id)"
                                        title="这是一段内容确定删除吗？"
                                >
                            <el-button type="text" slot="reference" size="small" icon="el-icon-delete">删除</el-button>
                        </el-popconfirm>
                        </span>

                        <!--                        给审核员使用的按钮-->
                        <span v-if="scope.row.status==2">
                          <el-popconfirm
                                  style="margin-left:20px;"
                                  @onConfirm="publish(scope.row.id)"
                                  title="审核通过后库存将更新,是否通过"
                          >
                        <el-button type="text" slot="reference" size="small" icon="el-icon-refresh">通过</el-button>
                    </el-popconfirm>
                        <el-popconfirm
                                style="margin-left:20px;"
                                @onConfirm="del(scope.row.id)"
                                title="这是一段内容确定删除吗？"
                        >
                            <el-button type="text" slot="reference" size="small" icon="el-icon-delete">删除</el-button>
                        </el-popconfirm>
                        </span>

                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页部分 -->
            <el-pagination
                    style="margin-top:10px;"
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryMap.pageNum"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>
            <!-- 入库明细 -->
            <el-dialog title="入库明细" :visible.sync="dialogVisible" width="50%">
                <el-steps v-if="pStatus==0"  style="margin-left: 150px;margin-bottom: 5px;" :space="200" :active="3" finish-status="success">
                    <el-step title="提交入库单" ></el-step>
                    <el-step title="审核入库单"></el-step>
                    <el-step title="进入库存"></el-step>
                </el-steps>
                <el-steps v-if="pStatus==2"  style="margin-left: 150px;margin-bottom: 5px;" :space="200" :active="2" finish-status="success">
                    <el-step title="提交入库单" ></el-step>
                    <el-step title="审核入库单"></el-step>
                    <el-step title="进入库存"></el-step>
                </el-steps>
        <span>
          <template>
            <el-table max-height="350" border :data="detailTable" style="width: 100%">
              <el-table-column prop="name" label="名称"></el-table-column>
              <el-table-column prop="pnum" label="商品编号"></el-table-column>
               <el-table-column prop="model" label="规格"></el-table-column>
              <el-table-column
                      prop="imageUrl"
                      label="图片"
                      align="center"
                      width="150px"
                      padding="0px"
              >
                <template slot-scope="scope">
                  <img
                          :src="'https://www.zykhome.club/'+scope.row.imageUrl"
                          alt
                          style="width: 30px;height:30px"
                  />
                </template>
              </el-table-column>
               <el-table-column prop="count" label="数量"></el-table-column>
                <el-table-column prop="unit" label="单位"></el-table-column>
            </el-table>

          </template>
        </span>
                <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                loading: true,
                detailTable: [],
                dialogVisible: false,
                total: 0,
                queryMap: {pageNum: 1, pageSize: 10, status: 0},
                tableData: [],
                pStatus:'',//步骤flag
            };
        },
        methods: {
            /**
             *物资入库审核
             */
             async publish(id){
                const { data: res } = await this.$http.put("inStock/publish/"+id);
                if (res.code !== 200) {
                    return this.$message.error("审核失败:"+res.msg);
                } else {
                    this.loadTableData();
                    return this.$message.success("入库已审核通过");
                }
            },
            /**
             * 从回收站恢复
             */
            async back(id){
                const { data: res } = await this.$http.put("inStock/back/"+id);
                if (res.code !== 200) {
                    return this.$message.error("从回收站恢复失败:"+res.msg);
                } else {
                    this.loadTableData();
                    return this.$message.success("从回收站中已恢复");
                }
            },
            /**
             * 移除回收站
             */
            async remove(id) {
                const {data: res} = await this.$http.put("inStock/remove/" + id);
                if (res.code !== 200) {
                    return this.$message.error("移入回收站失败:" + res.msg);
                } else {
                    this.loadTableData();
                    return this.$message.success("移入回收站成功");
                }
            },
            /**删除明细
             */
            async del(id) {
                const {data: res} = await this.$http.get("inStock/delete/" + id);
                if (res.code !== 200) {
                    return this.$message.error("删除失败:" + res.msg);
                } else {
                    this.loadTableData();
                    return this.$message.success("删除入库单记录成功");
                }
            },
            /**
             * 查看入库单明细
             */
            async detail(id) {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                const {data: res} = await this.$http.get("inStock/detail/" + id);
                if (res.code !== 200) {
                    this.$message.error("获取明细失败:" + res.msg);
                    loading.close();
                } else {
                    this.detailTable = res.data.itemVOS;
                    this.pStatus=res.data.status;
                    setTimeout(() => {
                        loading.close();
                        this.dialogVisible = true;
                    }, 500);
                }

            },
            /**
             * 加载表格数据
             */
            async loadTableData() {
                const {data: res} = await this.$http.get("inStock/findInStockList", {
                    params: this.queryMap
                });
                if (res.code !== 200) {
                    return this.$message.error("获取列表失败");
                } else {
                    this.total = res.data.total;
                    this.tableData = res.data.rows;
                }
            },
            /**
             * 改变页码
             */
            handleSizeChange(newSize) {
                this.queryMap.pageSize = newSize;
                this.loadTableData();
            },
            /**
             * 查询入库单
             */
            search() {
                this.queryMap.pageNum = 1;
                this.loadTableData();
            },
            /**
             * 点击分页
             */
            handleCurrentChange(current) {
                this.queryMap.pageNum = current;
                this.loadTableData();
            }
        },
        created() {
            this.loadTableData();
            setTimeout(() => {
                this.loading = false;
            }, 1000);
        }
    };
</script>

<style>
</style>