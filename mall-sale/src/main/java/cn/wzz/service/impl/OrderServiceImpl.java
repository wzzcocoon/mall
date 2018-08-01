package cn.wzz.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wzz.bean.OBJECT_T_MALL_FLOW;
import cn.wzz.bean.OBJECT_T_MALL_ORDER;
import cn.wzz.bean.T_MALL_ADDRESS;
import cn.wzz.bean.T_MALL_ORDER_INFO;
import cn.wzz.exception.OverSaleException;
import cn.wzz.mapper.OrderMapper;
import cn.wzz.service.OrderService;
import cn.wzz.util.MyDataUtil;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public void save_order(OBJECT_T_MALL_ORDER order, T_MALL_ADDRESS address) {
		List<Integer> list_id = new ArrayList<Integer>();
		
		Map<Object,Object> map_order = new HashMap<Object,Object>();
		map_order.put("order", order);
		map_order.put("address", address);
		// 1.插入订单信息，返回订单的主键
		orderMapper.insert_order(map_order);	
		
		List<OBJECT_T_MALL_FLOW> list_flow = order.getList_flow();
		for (int i = 0; i < list_flow.size(); i++) {
			// 2.循环插入每一个物流信息
			OBJECT_T_MALL_FLOW flow = list_flow.get(i);
			flow.setMdd(address.getYh_dz());
			Map<Object,Object> map_flow = new HashMap<Object,Object>();
			map_flow.put("dd_id", order.getId());
			map_flow.put("flow", flow);
			orderMapper.insert_flow(map_flow);
			
			// 3.批量插入订单信息
			List<T_MALL_ORDER_INFO> list_info = flow.getList_info();
			Map<Object,Object> map_info = new HashMap<Object,Object>();
			map_info.put("list_info", list_info);
			map_info.put("dd_id", order.getId());
			map_info.put("flow_id", flow.getId());
			orderMapper.insert_infos(map_info);
			
			for (int j = 0; j < list_info.size(); j++) {
				list_id.add(list_info.get(j).getGwch_id());
			}
		}
		
		// 4.删除已经生成的订单的购物车对象
		Map<Object,Object> map_cart = new HashMap<Object,Object>();
		map_cart.put("list_id", list_id);
		orderMapper.delete_carts(map_cart);
	}

	
	@Override
	public void paySuccess(OBJECT_T_MALL_ORDER order) throws OverSaleException {
		// 修改订单状态-已支付、 进度号
		order.setJdh(2);
		orderMapper.update_order(order);
		// 修改订单信息
		
		// 修改物流信息
		List<OBJECT_T_MALL_FLOW> list_flow = order.getList_flow();
//伪代码
//		for (int i = 0; i < list_flow.size(); i++) {
//			OBJECT_T_MALL_FLOW flow = list_flow.get(i);
//
//			List<T_MALL_ORDER_INFO> list_info = flow.getList_info();
//			// 修改sku数据量和销量等信息
//			for (int j = 0; j < list_info.size(); j++) {
//				T_MALL_ORDER_INFO info = list_info.get(j);
//
//				// 查询库存的业务
//				long kc = 0;
//				// 判断库存警戒线,才决定是否解决超卖问题
//				long count = 0;
//				if (count == 0) {
//					kc = 1;// 执行锁sql
//				} else {
//					kc = 1;// 执行不锁sql
//				}
//				if (kc > info.getSku_shl()) {// 先确定kc大于购买数量
//					// 对kc进行修改
//				} else {
//					throw new OverSaleException("over sale");
//				}
//			}
//		}
		for (int i = 0; i < list_flow.size(); i++) {
			OBJECT_T_MALL_FLOW flow = list_flow.get(i);
			//修改物流业务
			flow.setPsshj(MyDataUtil.getMyDate(1));
			flow.setPsmsh("蜂鸟配送");
			flow.setMqdd("商品已出库");
			flow.setMdd(order.getDzh_mch());
			flow.setYwy("配送员" + i);
			flow.setLxfsh("123123123123");
			orderMapper.update_flow(flow);
			
			List<T_MALL_ORDER_INFO> list_info = flow.getList_info();
			// 修改sku数据量和销量等信息
			for (int j = 0; j < list_info.size(); j++) {
				T_MALL_ORDER_INFO info = list_info.get(j);

				// 判断警戒线
				long count = orderMapper.select_count_kc(info.getSku_id());
				
				// 查询库存剩余数量
				long kc = getKc(count,info.getSku_id());
				
				// 先判断库存大于购买数量
				if (kc >= info.getSku_shl()) {
					// 修改库存数量
					orderMapper.update_kc(info);
				} else {
					// 事务回滚，返回订单失败通知，购买数量大于库存
					throw new OverSaleException("over sale");
				}
			}
		}
		
		// 修改订单状态 -出库：预计送达时间
		order.setYjsdshj(MyDataUtil.getMyDate(3));
		orderMapper.update_order(order);
	}


	private long getKc(long count,int sku_id) {
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("count", count);
		map.put("sku_id", sku_id);
		
		long kc = orderMapper.select_kc(map);
		return kc;
	}
	
}
