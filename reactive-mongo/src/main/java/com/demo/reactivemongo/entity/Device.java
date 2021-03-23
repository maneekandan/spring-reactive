package com.demo.reactivemongo.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author admin
 *
 */
@Document
public class Device {

    @Id
    private String id;
    private String deviceid;
    private String devicetype;
    private String serailnumber;
    private String status;
    private double value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getSerailnumber() {
		return serailnumber;
	}
	public void setSerailnumber(String serailnumber) {
		this.serailnumber = serailnumber;
	}

}
