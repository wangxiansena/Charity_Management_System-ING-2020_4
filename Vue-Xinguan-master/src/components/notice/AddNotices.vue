<template>
  <div class="addNotices">
    <!-- 面包导航 -->
    <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>公告管理</el-breadcrumb-item>
      <el-breadcrumb-item>公告发布</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row style="margin-bottom:10px;" :gutter="20">
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="公告标题">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
            <el-form-item label="摘要信息">
                 <el-col :span="9">
                      <el-input type="textarea" v-model="form.desc"></el-input>
                 </el-col>
                    <el-col :span="5" style="margin-top:14px;margin-left:20px;">
                        <el-rate show-text v-model="form.value2"></el-rate>
                    </el-col>
            <el-col :span="4" style="margin-top:10px;">
              <el-switch
                style="display: block"
                v-model="form.comment"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="开启评论"
                inactive-text="禁止评论"
              ></el-switch>
            </el-col>
        </el-form-item>

          <el-form-item label="有效时间">
            <el-col :span="9">
              <el-date-picker
                v-model="value1"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
            </el-col>
          </el-form-item>
        
         <!-- Two-way Data-Binding -->
      <quill-editor
        ref="myQuillEditor"
        v-model="content"
        :options="editorOption"
        @blur="onEditorBlur($event)"
        @focus="onEditorFocus($event)"
        @ready="onEditorReady($event)"
      />
      
    
      <el-row style="margin-top:30px;">
        <el-col>
          <el-button type="primary">立即发布</el-button>
        </el-col>
      </el-row>
        </el-form>
      </el-row>

     
    </el-card>
  </div>
</template>

<style>
.ql-editor {
  min-height: 300px;
}
</style>

<script>
export default {
  data() {
    return {
      form: {}
    };
  }
};
</script>