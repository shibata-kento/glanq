package com.internousdev.glanq.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.glanq.dao.DestinationInfoDAO;
import com.internousdev.glanq.dto.DestinationInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class LocationChoiceConfirmAction extends ActionSupport implements SessionAware{

	private String categoryId;
	private Map<String, Object> session;
	private int parkId;

	public String execute(){

		String result = ERROR;

		DestinationInfoDAO destinationInfoDAO = new DestinationInfoDAO();
		List<DestinationInfoDTO> destinationInfoDtoList = new ArrayList<>();
		try {
			destinationInfoDtoList = destinationInfoDAO.getDestinationInfoFromId(parkId);
			Iterator<DestinationInfoDTO> iterator = destinationInfoDtoList.iterator();
			if(!(iterator.hasNext())) {
				destinationInfoDtoList = null;
			}
			session.put("parkId", parkId);
			session.put("destinationInfoDtoList", destinationInfoDtoList);
			result = SUCCESS;

		}catch(Exception e){
			e.printStackTrace();
			result = ERROR;
		}

		return result;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
	}


}
