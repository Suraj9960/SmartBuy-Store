package com.ecommerence_website.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BrandDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pid;

	/* common */
	private  String brand_name;

	private  String operating_system;

	private  String processor_brand;

	private String battery_capacity;

	private  String ram;

	private  String internal_storage;

	/* Mobile Phones */

	private  String primary_camera;

	private  String secondary_camera;

	private  String sim_type;

	private  String newtwork_type;

	private  String resolution_type;

	/* Earphones */

	private String type;

	private String compatible_with;

	private String headphone_type;

	/* SmartWatch */

	private String dial_shape;

	private String display_size;

	private String ideal_for;

	private String display_type;

	/* Laptop & computers */

	private String processor_with_generation;

	private String storage_type;

	private String graphics_memory_type;
	
	

}
