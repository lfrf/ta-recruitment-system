package com.group27.tarecruitment.model;
/**
 * UserAccount class type.
 *
 * <p>Model type that represents structured domain data and state fields.</p>
 * <p>Package: {@code com.group27.tarecruitment.model}</p>
 */
public class UserAccount { private String userId; private String username; private String password; private UserRole role; private String displayName; private String email; private boolean active; public String getUserId(){return userId;} public void setUserId(String userId){this.userId=userId;} public String getUsername(){return username;} public void setUsername(String username){this.username=username;} public String getPassword(){return password;} public void setPassword(String password){this.password=password;} public UserRole getRole(){return role;} public void setRole(UserRole role){this.role=role;} public String getDisplayName(){return displayName;} public void setDisplayName(String displayName){this.displayName=displayName;} public String getEmail(){return email;} public void setEmail(String email){this.email=email;} public boolean isActive(){return active;} public void setActive(boolean active){this.active=active;} }
