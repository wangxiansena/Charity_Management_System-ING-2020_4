package com.coderman.api.system.service.impl;

import com.coderman.api.biz.converter.ProductCategoryConverter;
import com.coderman.api.biz.mapper.ProductCategoryMapper;
import com.coderman.api.biz.pojo.ProductCategory;
import com.coderman.api.biz.service.ProductCategoryService;
import com.coderman.api.biz.utils.CategoryTreeBuilder;
import com.coderman.api.biz.utils.ListPageUtils;
import com.coderman.api.biz.vo.ProductCategoryTreeNodeVO;
import com.coderman.api.biz.vo.ProductCategoryVO;
import com.coderman.api.system.vo.PageVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @Author team
 * @Date 2020/4/16 17:19
 * @Version 1.0
 **/
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {


    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    /**
     * 商品类别列表
     * @param pageNum
     * @param pageSize
     * @param ProductCategoryVO
     * @return
     */
    @Override
    public PageVO<ProductCategoryVO> findProductCategoryList(Integer pageNum, Integer pageSize, ProductCategoryVO ProductCategoryVO) {
        PageHelper.startPage(pageNum, pageSize);
        Example o = new Example(ProductCategory.class);
        Example.Criteria criteria = o.createCriteria();
        o.setOrderByClause("sort asc");
        if (ProductCategoryVO.getName() != null && !"".equals(ProductCategoryVO.getName())) {
            criteria.andLike("name", "%" + ProductCategoryVO.getName() + "%");
        }
        List<ProductCategory> productCategories = productCategoryMapper.selectByExample(o);
        List<ProductCategoryVO> categoryVOS=ProductCategoryConverter.converterToVOList(productCategories);
        PageInfo<ProductCategory> info = new PageInfo<>(productCategories);

        return new PageVO<>(info.getTotal(), categoryVOS);
    }


    /**
     * 添加商品类别
     * @param ProductCategoryVO
     */
    @Override
    public void add(ProductCategoryVO ProductCategoryVO) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(ProductCategoryVO,productCategory);
        productCategory.setCreateTime(new Date());
        productCategory.setModifiedTime(new Date());
        productCategoryMapper.insert(productCategory);
    }

    /**
     * 编辑商品类别
     * @param id
     * @return
     */
    @Override
    public ProductCategoryVO edit(Long id) {
        ProductCategory productCategory = productCategoryMapper.selectByPrimaryKey(id);
        return  ProductCategoryConverter.converterToProductCategoryVO(productCategory);
    }

    /**
     * 更新商品类别
     * @param id
     * @param ProductCategoryVO
     */
    @Override
    public void update(Long id, ProductCategoryVO ProductCategoryVO) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(ProductCategoryVO,productCategory);
        productCategory.setModifiedTime(new Date());
        productCategoryMapper.updateByPrimaryKeySelective(productCategory);
    }

    /**
     * 删除商品类别
     * @param id
     */
    @Override
    public void delete(Long id) {
        productCategoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 所有商品类别
     * @return
     */
    @Override
    public List<ProductCategoryVO> findAll() {
        List<ProductCategory> productCategories = productCategoryMapper.selectAll();
        return ProductCategoryConverter.converterToVOList(productCategories);
    }

    /**
     * 分类树形结构
     * @return
     */
    @Override
    public PageVO<ProductCategoryTreeNodeVO> categoryTree(Integer pageNum,Integer pageSize) {
        List<ProductCategoryVO> productCategoryVOList = findAll();
        List<ProductCategoryTreeNodeVO> nodeVOS=ProductCategoryConverter.converterToTreeNodeVO(productCategoryVOList);
        List<ProductCategoryTreeNodeVO> tree = CategoryTreeBuilder.build(nodeVOS);
        List<ProductCategoryTreeNodeVO> page;
        if(pageSize!=null&&pageNum!=null){
            page= ListPageUtils.page(tree, pageSize, pageNum);
            return new PageVO<>(tree.size(),page);
        }else {
            return new PageVO<>(tree.size(), tree);
        }
    }

    /**
     * 获取父级分类（2级树）
     * @return
     */
    @Override
    public List<ProductCategoryTreeNodeVO> getParentCategoryTree() {
        List<ProductCategoryVO> productCategoryVOList = findAll();
        List<ProductCategoryTreeNodeVO> nodeVOS=ProductCategoryConverter.converterToTreeNodeVO(productCategoryVOList);
        return  CategoryTreeBuilder.buildParent(nodeVOS);
    }

}
