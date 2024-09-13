package com.library.bookwave.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.library.bookwave.dto.BalanceHistoryDTO;
import com.library.bookwave.dto.Payment2DTO;
import com.library.bookwave.dto.SignUpDTO;
import com.library.bookwave.repository.model.BalanceHistory;
import com.library.bookwave.repository.model.Payment;

@Mapper
public interface MyPageRepository {
	public int updateUserByPassword (@Param ("password") String password, @Param("id") Integer id);
	
	public SignUpDTO findUserInfoByuserId (Integer userId);
	
	public void updateAddress (@Param ("zip") String zip, @Param("addr1") String addr1,@Param("addr2") String addr2,@Param("userId") Integer userId);
	
	public void updatePhone (@Param ("phone") String phone, @Param("userId") Integer userId);
	
	public void deleteUserDetail (Integer userId);
	
	public void deleteUser (Integer Id);
	
	public List<BalanceHistoryDTO> findWaveHistory (@Param("userId")Integer userId,@Param("limit") Integer limit, @Param("offset") Integer offset,@Param("sortBy") String sortBy,@Param("sortOrder") String sortOrder);
	
	public List<BalanceHistoryDTO> findMileageHistory (@Param("userId") Integer userId,@Param("limit") Integer limit, @Param("offset") Integer offset,@Param("sortBy") String sortBy,@Param("sortOrder") String sortOrder);
	
	public List<BalanceHistoryDTO> findBalanceHistory(@Param("userId") Integer userId,@Param("limit") Integer limit, @Param("offset") Integer offset,@Param("sortBy") String sortBy,@Param("sortOrder") String sortOrder,@Param("historyType") String historyType);
	
	public Integer countWaveHistory (Integer userId);
	
	public Integer countMileageHistory (Integer userId);
	
	public Integer countBalanceHistory(Integer userId);
	// TODO 변경 후
	public Integer countBalanceHistory(@Param("userId") Integer userId,@Param("historyType") String historyType);
	
	public List<Payment2DTO> findPaymentByUserId (Integer userId);
	
	public Payment2DTO findPaymentById (Integer id);
	
	public void updatePayment (@Param("id") Integer id,@Param("cancelReason") String cancelReason);
	
}
