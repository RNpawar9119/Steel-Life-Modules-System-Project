//package com.jwt.tok.model;
//
//import java.util.Set;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.ManyToMany;
//import javax.persistence.Table;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//@Entity
//@Table(name = "menus")
//public class Menu {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	@Column(nullable = false, unique = true)
//	private String menuName; // Dashboard / Orders / Products etc.
//
//	private String description;
//
//	// Role mapping (Many-to-Many)
//	@ManyToMany(mappedBy = "menus")
//	@JsonBackReference
//	private Set<Role> roles;
//
//	// ✅ Getters & Setters
//	public Long getId() {
//		return id;
//	}
//
//	public String getMenuName() {
//		return menuName;
//	}
//
//	public void setMenuName(String menuName) {
//		this.menuName = menuName;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public Set<Role> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(Set<Role> roles) {
//		this.roles = roles;
//	}
//}
package com.jwt.tok.model;

import java.util.Set;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String menuName;

    private String description;

    @ManyToMany(mappedBy = "menus")
    @JsonBackReference
    private Set<Role> roles;

    // getters setters

    // Getters & Setters
    public Long getId() { return id; }
    public String getMenuName() { return menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
