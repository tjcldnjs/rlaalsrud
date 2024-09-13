package com.library.bookwave.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.bookwave.dto.BalanceHistoryDTO;
import com.library.bookwave.dto.Payment2DTO;
import com.library.bookwave.dto.SignUpDTO;
import com.library.bookwave.repository.interfaces.MyPageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

	private final MyPageRepository myPageRepository;

	public List<BalanceHistoryDTO> findBalanceHistory(Integer userId, Integer page, Integer size, String sortBy,
			String sortOrder,String historyType) {
		List<BalanceHistoryDTO> balanceHistory = null;
		try {
			int limit = size;
			int offset = (page - 1) * size;
			balanceHistory = myPageRepository.findBalanceHistory(userId, limit, offset, sortBy, sortOrder,historyType);
		} catch (Exception e) {
			// TODO 
		}

		return balanceHistory;
	}

	@Transactional
	public boolean updateUserByPassword(String newPassword, Integer id) {
		int result = 0;
		try {
			result = myPageRepository.updateUserByPassword(newPassword, id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	public SignUpDTO findUserInfoByuserId(Integer userId) {
		SignUpDTO signUpDTO = null;
		try {
			signUpDTO = myPageRepository.findUserInfoByuserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signUpDTO;
	}

	@Transactional
	public void updateAddress(String zip, String addr1, String addr2, Integer userId) {
		try {
			myPageRepository.updateAddress(zip, addr1, addr2, userId);
		} catch (Exception e) {
		}
	}

	@Transactional
	public void updatePhone(String phone, Integer userId) {
		try {
			myPageRepository.updatePhone(phone, userId);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Transactional
	public void deleteUserAccount(Integer userId) {
		try {
			myPageRepository.deleteUserDetail(userId);
			myPageRepository.deleteUser(userId);
		} catch (Exception e) {
		}

	}

	public List<BalanceHistoryDTO> findWaveHistory(Integer userId, Integer page, Integer size, String sortBy,
			String sortOrder) {
		List<BalanceHistoryDTO> waveHistory = null;
		try {
			int limit = size;
			int offset = (page - 1) * size;
			waveHistory = myPageRepository.findWaveHistory(userId, limit, offset, sortBy, sortOrder);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return waveHistory;
	}

	public List<BalanceHistoryDTO> findMileageHistory(Integer userId, Integer page, Integer size, String sortBy,
			String sortOrder) {
		List<BalanceHistoryDTO> mileageHistory = null;
		try {
			int limit = size;
			int offset = (page - 1) * size;
			mileageHistory = myPageRepository.findMileageHistory(userId, limit, offset, sortBy, sortOrder);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mileageHistory;
	}

	public int countBalanceHistory(Integer userId, String historyType) {
		int count = 0;
		try {
			count = myPageRepository.countBalanceHistory(userId, historyType);
		} catch (Exception e) {
		}
		return count;
	}

	public int countWaveHistory(Integer userId) {
		int countWaveHistory = 0;
		try {
			countWaveHistory = myPageRepository.countWaveHistory(userId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return countWaveHistory;
	}

	public int countMileageHistory(Integer userId) {
		int countMileageHistory = 0;
		try {
			countMileageHistory = myPageRepository.countMileageHistory(userId);
		} catch (Exception e) {
		}
		return countMileageHistory;
	}

	public List<Payment2DTO> findPaymentByUserId(Integer userId) {
		List<Payment2DTO> payment = null;
		try {
			payment = myPageRepository.findPaymentByUserId(userId);
		} catch (Exception e) {
		}
		return payment;
	}
	
	public Payment2DTO findPaymentById (Integer id) {
		Payment2DTO payment = null;
		try {
			payment = myPageRepository.findPaymentById(id);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return payment;
	}
	
	public void updatePayment (Integer id, String cancelReason) {
		try {
			myPageRepository.updatePayment(id,cancelReason);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	

}
