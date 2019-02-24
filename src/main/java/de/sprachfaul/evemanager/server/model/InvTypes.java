package de.sprachfaul.evemanager.server.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "invtypes")
public class InvTypes {
	@Id
	private Integer typeID;
	
	@Column(name = "groupid")
	private Integer groupID;
	
	@Column(name = "typename")
	private String typeName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "mass")
	private Double mass;
	
	@Column(name = "volume")
	private Double volume;
	
	@Column(name = "capacity")
	private Double capacity;
	
	@Column(name = "portionsize")
	private Integer portionSize;
	
	@Column(name = "raceid")
	private Integer raceID;
	
	@Column(name = "baseprice")
	private BigDecimal basePrice;
	
	@Column(name = "published")
	private Boolean published;
	
	@Column(name = "marketgroupid")
	private Integer marketGroupID;
	
	@Column(name = "iconid")
	private Integer iconID;
	
	@Column(name = "soundid")
	private Integer soundID;
	
	@Column(name = "graphicid")
	private Integer graphicID;

	@Override
	public String toString() {
		return "InvTypes [typeID=" + typeID + ", groupID=" + groupID + ", typeName=" + typeName + ", description="
				+ description + ", mass=" + mass + ", volume=" + volume + ", capacity=" + capacity + ", portionSize="
				+ portionSize + ", raceID=" + raceID + ", basePrice=" + basePrice + ", published=" + published
				+ ", marketGroupID=" + marketGroupID + ", iconID=" + iconID + ", soundID=" + soundID + ", graphicID="
				+ graphicID + "]";
	}

	public Integer getTypeID() {
		return typeID;
	}

	public void setTypeID(Integer typeID) {
		this.typeID = typeID;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getMass() {
		return mass;
	}

	public void setMass(Double mass) {
		this.mass = mass;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getCapacity() {
		return capacity;
	}

	public void setCapacity(Double capacity) {
		this.capacity = capacity;
	}

	public Integer getPortionSize() {
		return portionSize;
	}

	public void setPortionSize(Integer portionSize) {
		this.portionSize = portionSize;
	}

	public Integer getRaceID() {
		return raceID;
	}

	public void setRaceID(Integer raceID) {
		this.raceID = raceID;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	public Integer getMarketGroupID() {
		return marketGroupID;
	}

	public void setMarketGroupID(Integer marketGroupID) {
		this.marketGroupID = marketGroupID;
	}

	public Integer getIconID() {
		return iconID;
	}

	public void setIconID(Integer iconID) {
		this.iconID = iconID;
	}

	public Integer getSoundID() {
		return soundID;
	}

	public void setSoundID(Integer soundID) {
		this.soundID = soundID;
	}

	public Integer getGraphicID() {
		return graphicID;
	}

	public void setGraphicID(Integer graphicID) {
		this.graphicID = graphicID;
	}
	
	
}
