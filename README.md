# <h1 align="center">Project Spring And Spring Boot</h1>

A project to study and apply knowledge of Spring and Spring Boot <br>

## Purpose of the project: 
After spending time learning about web development, web development with the Spring Framework, and working on small projects using Spring technologies such as Spring Boot, Spring Data, and Spring Security, I wanted to build a complete project to apply all the knowledge I’ve learned. I chose an e-commerce topic because it is popular and has plenty of tutorials available for beginners like me. 

## How to Understand the Project
1. Start with ShopmeCommon.
This module defines all the entity classes and their relationships. By understanding the data model first, you will get a solid foundation for how the system works internally.
2. Move to ShopmeBackEnd.
This is the admin panel where all management functionalities (products, users, categories, orders, etc.) are implemented. Review the controllers, services, and templates to understand how administrative features are handled.
3. Explore ShopmeFrontEnd. 
This is the part visible to end-users. Start by looking at the home page controller and templates. From there, follow the navigation and features such as login, product browsing, and ordering.
4. Check Configuration Files
Review the application.properties or application.yml, Spring Security configuration, and Thymeleaf settings to understand how everything is wired together.

## Project Structure
The project consists of three main parts:
- ShopmeCommon
- ShopmeWebParent
  - ShopmeFrontEnd
  - ShopmeBackEnd

## ShopmeCommon
- This module contains all entity classes and their mappings to the database.
- When a new developer wants to understand what entities are used in the project and how they are related, they only need to look into this module. It helps avoid being overwhelmed by unrelated frontend or backend source code.

## ShopmeWebParent
- This is the core folder of the project. It contains two submodules: ShopmeFrontEnd and ShopmeBackEnd.
- When a developer wants to explore the features on the frontend or backend, they should start from this folder.

## ShopmeBackEnd
- This is the admin side of the system.
- It is used for managing products, categories, brands, users, shipping, orders, and other resources of the e-commerce platform.
- Admin users can perform CRUD operations, view dashboards, manage account permissions, and monitor overall system activities.
- This part is secured using Spring Security, with features like login, role-based access control, and password encryption.

## ShopmeFrontEnd
- This is the user-facing side of the system.
- It allows customers to browse the online store, search for products, view product details, register an account, log in, and place orders.
- Users can also manage their profiles, view order history, and track the status of their purchases.
- The frontend is designed to provide a smooth and intuitive shopping experience, with features such as pagination, sorting, filtering, and cart management.








 
  
 









 

