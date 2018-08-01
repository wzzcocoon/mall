package cn.wzz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.wzz.bean.OBJECT_T_MALL_ATTR;
import cn.wzz.bean.T_MALL_VALUE;

/**这次Mybatis传参使用的多参数列表，并没有传递Map集合*/
public interface AttrMapper {

	void insert_attr(@Param("flbh2") int flbh2, @Param("attr") OBJECT_T_MALL_ATTR attr);

	void insert_values(@Param("attr_id") int attr_id, @Param("list_value") List<T_MALL_VALUE> list_value);

	List<OBJECT_T_MALL_ATTR> select_attr_list(int flbh2);


}
