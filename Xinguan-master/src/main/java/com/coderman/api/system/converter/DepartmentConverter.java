package com.coderman.api.system.converter;

import com.coderman.api.system.pojo.Department;
import com.coderman.api.system.vo.DepartmentVO;
import org.springframework.beans.BeanUtils;

/**
 * @Author team
 * @Date 2020/4/7 19:56
 * @Version 1.0
 **/
public class DepartmentConverter {


    /**
     * 转vo
     * @return
     */
    public static DepartmentVO converterToDepartmentVO(Department department){
        DepartmentVO departmentVO = new DepartmentVO();
        BeanUtils.copyProperties(department,departmentVO);
        return departmentVO;
    }
}
