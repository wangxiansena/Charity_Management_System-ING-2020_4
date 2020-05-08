<template>
  <div id="welcome">
    <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>欢迎</el-breadcrumb-item>
    </el-breadcrumb>
    <el-row :gutter="15">
      <!-- 左边部分 -->
      <el-col :span="13">
        <!-- 用户信息表格 -->
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>用户信息</span>
            <el-button style="float: right;" size="mini" type="danger">用户中心</el-button>
          </div>
          <el-tooltip class="item" effect="dark" content="Top Left 提示文字" placement="top-start">
            <el-avatar
              shape="square"
              :size="90"
              :src="userInfo.avatar"
              style="float:left;"
              :key="1"
            ></el-avatar>
          </el-tooltip>
          <div class="right" style="float:right;width:520px;">
            <el-table :data="tableInfo" border height="120">
              <el-table-column prop="username" label="用户账号"></el-table-column>
              <el-table-column prop="nickname" label="用户昵称"></el-table-column>
              <el-table-column prop="department" label="所属部门"></el-table-column>
              <el-table-column fixed="right" prop="roles" label="用户角色" width="150"></el-table-column>
            </el-table>
          </div>
        </el-card>
        <!-- 功能列表 -->
        <el-row style="margin-top:10px;" :gutter="10">
          <el-card style="height:125px;">
            <el-col :span="6">
              <div class="grid-content bg-purple">
                <router-link to="/products">
                  <img
                    src="../assets/11.png"
                    alt
                    width="60.8px;"
                    style="margin:0px auto; cursor: pointer;margin-left:20px;"
                  />
                </router-link>
                <div style="font-size:12px;margin-top:5px;margin-left:25px;">物资资料</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="grid-content bg-purple-light">
                <router-link to="/stocks">
                  <img
                    src="../assets/22.png"
                    alt
                    width="60.8px;"
                    style="cursor: pointer;margin-left:20px;"
                  />
                </router-link>
                <div style="font-size:12px;margin-top:5px;margin-left:25px;">物资库存</div>
              </div>
            </el-col>

            <el-col :span="6">
              <div class="grid-content bg-purple-light">
                <router-link to="/inStocks">
                  <img
                    src="../assets/44.png"
                    alt
                    width="60.8px;"
                    style="cursor: pointer;margin-left:20px;"
                  />
                </router-link>

                <div style="font-size:12px;margin-top:5px;margin-left:25px;">物资入库</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="grid-content bg-purple"></div>

              <img
                src="../assets/33.png"
                alt
                width="60.8px;"
                style="cursor: pointer;margin-left:20px;"
              />
              <div style="font-size:12px;margin-top:5px;margin-left:25px;">物资发放</div>
            </el-col>
          </el-card>
        </el-row>
        <el-card class="box-card" style="margin-top:20px;padding:0px;">
          <!-- 用户登入报表 -->
          <div id="loginReport" style="width: 650px;height:270px;"></div>
        </el-card>
      </el-col>
      <!-- 右边部分 -->
      <el-col :span="11">
        <div class="grid-content bg-purple">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>日历</span>
              <el-button style="float: right; padding: 3px 0" type="text">添加日程</el-button>
            </div>
            <div class="text item">
              <el-calendar>
                <!-- 这里使用的是 2.5 slot 语法，对于新项目请使用 2.6 slot 语法-->
                <template slot="dateCell" slot-scope="{date, data}">
                  <p
                    :class="data.isSelected ? 'is-selected' : ''"
                  >{{ data.day.split('-').slice(1).join('-') }} {{ data.isSelected ? '✔️' : ''}}</p>
                </template>
              </el-calendar>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <!-- <el-card class="box-card">
           <el-calendar v-model="value"></el-calendar>
    </el-card>-->
  </div>
</template>
<script>
import echarts from "echarts";
export default {
  data() {
    return {
      xData: [],
      yData: [],
      myData:[],
      value: new Date(),
      userInfo: {},
      tableInfo: []
    };
  },
  methods: {
    /**
     * 加载登入报表数据
     */
    async loginReport(username) {
      const { data: res } = await this.$http.post("loginLog/loginReport",{username:username});
      if (res.code !== 200) {
        return this.$message.error("获取登入报表数据失败:"+res.msg);
      } else {
        var $this = this;
        this.xData = [];
        this.yData = [];
        this.myData=[];
        res.data.all.forEach(e1 => {
          $this.xData.push(e1.days);
          $this.yData.push(e1.count);
        });

        for(var i=0;i<this.xData.length;i++){
          var count=0;
          for(var j=0;j<res.data.me.length;j++){
            if($this.xData[i]===res.data.me[j].days){
              count=res.data.me[j].count;
              break;
            }else {
              count=0;
            }
          }
          $this.myData.push(count);
        }
      }
      this.draw();
    },

    /**
     * 绘制登入报表
     */
    draw() {
      var myChart = echarts.init(document.getElementById("loginReport"));
      // 指定图表的配置项和数据
      var option = {
        title: {
          text: "用户登入统计"
        },
        toolbox: {
          show: true,
          feature: {
            dataZoom: {
              yAxisIndex: "none"
            },
            dataView: { readOnly: false }, //  缩放
            magicType: { type: ["bar","line"] }, ///　　折线  直方图切换
            restore: {}, // 重置
            saveAsImage: {} // 导出图片
          }
        },
        tooltip: {},
        legend: {
          data: ["全部","我"]
        },
        xAxis: {
          data: this.xData
        },
        yAxis: {
          type: "value"
        },
        series: [
          {
            name:"全部",
            data: this.yData,
            type: "bar",
            showBackground: true,
            animationDuration: 900,
            animationEasing: "cubicInOut",
          },
           {
            name:"我",
            data: this.myData,
            type: "bar",
            showBackground: true,
            animationDuration: 900,
            animationEasing: "cubicInOut",
          }
        ]
      };
      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    }
  },

  created() {
    this.userInfo = this.$store.state.userInfo;
    var roles = this.userInfo.isAdmin ? "超级管理员" : this.userInfo.roles;
    this.tableInfo.push({
      username: this.userInfo.username,
      nickname: this.userInfo.nickname,
      department: this.userInfo.department,
      roles: roles
    });
  },
  mounted: function() {
    this.loginReport(this.userInfo.username);
    this.draw();
  }
};
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>
