/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.inventory.db;

import java.util.Collection;
import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Role;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.inventory.model.InventoryDrug;
import org.openmrs.module.inventory.model.InventoryDrugCategory;
import org.openmrs.module.inventory.model.InventoryDrugFormulation;
import org.openmrs.module.inventory.model.InventoryDrugUnit;
import org.openmrs.module.inventory.model.InventoryItem;
import org.openmrs.module.inventory.model.InventoryItemCategory;
import org.openmrs.module.inventory.model.InventoryItemSpecification;
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.openmrs.module.inventory.model.InventoryItemUnit;
import org.openmrs.module.inventory.model.InventoryStore;
import org.openmrs.module.inventory.model.InventoryStoreDrug;
import org.openmrs.module.inventory.model.InventoryStoreDrugAccount;
import org.openmrs.module.inventory.model.InventoryStoreDrugAccountDetail;
import org.openmrs.module.inventory.model.InventoryStoreDrugIndent;
import org.openmrs.module.inventory.model.InventoryStoreDrugIndentDetail;
import org.openmrs.module.inventory.model.InventoryStoreDrugPatient;
import org.openmrs.module.inventory.model.InventoryStoreDrugPatientDetail;
import org.openmrs.module.inventory.model.InventoryStoreDrugTransaction;
import org.openmrs.module.inventory.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.inventory.model.InventoryStoreItem;
import org.openmrs.module.inventory.model.InventoryStoreItemAccount;
import org.openmrs.module.inventory.model.InventoryStoreItemAccountDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemIndent;
import org.openmrs.module.inventory.model.InventoryStoreItemIndentDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemTransaction;
import org.openmrs.module.inventory.model.InventoryStoreItemTransactionDetail;


/**
 *
 */
public interface InventoryDAO {
	
	/**
	 * STORE 
	 */
	public void setSessionFactory(SessionFactory sessionFactory) throws DAOException;

    public List<InventoryStore> listInventoryStore(int min, int max) throws DAOException;
	
	public InventoryStore saveStore(InventoryStore store) throws DAOException;

	public int countListStore()  throws DAOException;
	
	public InventoryStore getStoreById(Integer id) throws DAOException;
	
	public InventoryStore getStoreByRole(String role) throws DAOException;
	
	public InventoryStore getStoreByCollectionRole(List<Role> roles) throws DAOException;
	
	public List<InventoryStore> listMainStore() throws DAOException;
	
	public void deleteStore(InventoryStore store) throws DAOException;
	
	public List<InventoryStore> listAllInventoryStore() throws DAOException;
	
	public InventoryStore getStoreByName(String name) throws DAOException;
	
	public List<InventoryStore> listStoreByMainStore(Integer storeid , boolean bothMainStore) throws DAOException ;
	
	/**
	 * ItemCategory
	 */
	 
	public List<InventoryItemCategory> listItemCategory(String name ,int min, int max) throws DAOException;
	
	public List<InventoryItemCategory> findItemCategory(String name) throws DAOException;
	
	public InventoryItemCategory saveItemCategory(InventoryItemCategory category) throws DAOException;

	public int countListItemCategory(String name)  throws DAOException;
	
	
	public InventoryItemCategory getItemCategoryById(Integer id) throws DAOException;
	
	
	public InventoryItemCategory getItemCategoryByName(String name) throws DAOException;
	
	
	public void deleteItemCategory(InventoryItemCategory category) throws DAOException;
	
	/**
	 * ItemSubCategory
	 */
	 
	
	public List<InventoryItemSubCategory> listItemSubCategory(String name ,int min, int max) throws DAOException;
	
	
	public List<InventoryItemSubCategory> findItemSubCategory(String name) throws DAOException;
	
	public List<InventoryItemSubCategory> listSubCatByCat(Integer categoryId) throws DAOException;
	
	public InventoryItemSubCategory saveItemSubCategory(InventoryItemSubCategory subCategory) throws DAOException;

	
	public int countListItemSubCategory(String name)  throws DAOException;
	
	
	public InventoryItemSubCategory getItemSubCategoryById(Integer id) throws DAOException;
	
	
	public InventoryItemSubCategory getItemSubCategoryByName(Integer categoryId ,String name) throws DAOException;
	
	public void deleteItemSubCategory(InventoryItemSubCategory subCategory) throws DAOException;
	
	/**
	 * ItemSpecification
	 */
	 
	
	public List<InventoryItemSpecification> listItemSpecification(String name ,int min, int max) throws DAOException;
	
	
	public List<InventoryItemSpecification> findItemSpecification(String name) throws DAOException;
	
	public InventoryItemSpecification saveItemSpecification(InventoryItemSpecification specification) throws DAOException;

	
	public int countListItemSpecification(String name)  throws DAOException;
	
	
	public InventoryItemSpecification getItemSpecificationById(Integer id) throws DAOException;
	
	
	public InventoryItemSpecification getItemSpecificationByName(String name) throws DAOException;
	
	public void deleteItemSpecification(InventoryItemSpecification specification) throws DAOException;
	
	/**
	 * ItemUnit
	 */
	 
	
	public List<InventoryItemUnit> listItemUnit(String name ,int min, int max) throws DAOException;
	
	
	public List<InventoryItemUnit> findItemUnit(String name) throws DAOException;
	
	public InventoryItemUnit saveItemUnit(InventoryItemUnit unit) throws DAOException;

	
	public int countListItemUnit(String name)  throws DAOException;
	
	
	public InventoryItemUnit getItemUnitById(Integer id) throws DAOException;
	
	
	public InventoryItemUnit getItemUnitByName(String name) throws DAOException;
	
	public void deleteItemUnit(InventoryItemUnit unit) throws DAOException;
	
	/**
	 * Item
	 */
	 
	
	public List<InventoryItem> listItem(Integer categoryId, String name ,int min, int max) throws DAOException;
	
	
	public List<InventoryItem> findItem(String name) throws DAOException;
	
	public InventoryItem saveItem(InventoryItem item) throws DAOException;

	
	public int countListItem(Integer categoryId, String name)  throws DAOException;
	
	
	public InventoryItem getItemById(Integer id) throws DAOException;
	
	
	public InventoryItem getItemByName(String name) throws DAOException;
	
	public void deleteItem(InventoryItem item) throws DAOException;
	
	public List<InventoryItem> findItem(Integer categoryId,String name) throws DAOException;
	
	public int countItem(Integer categoryId, Integer unitId,  Integer subCategoryId, Integer specificationId)  throws DAOException;
	
	
	/**
	 * Drug
	 */
	 
	
	public List<InventoryDrug> listDrug(Integer categoryId, String name ,int min, int max) throws DAOException;
	
	
	public List<InventoryDrug> findDrug(Integer categoryId,String name) throws DAOException;
	
	public InventoryDrug saveDrug(InventoryDrug drug) throws DAOException;

	
	public int countListDrug(Integer categoryId, String name)  throws DAOException;
	
	public int countListDrug(Integer categoryId, Integer unitId,  Integer formulationId)  throws DAOException;
	
	
	public InventoryDrug getDrugById(Integer id) throws DAOException;
	
	
	public InventoryDrug getDrugByName(String name) throws DAOException;
	
	public void deleteDrug(InventoryDrug drug) throws DAOException;
	
	
	/**
	 * DrugCategory
	 */
	 
	
	public List<InventoryDrugCategory> listDrugCategory(String name ,int min, int max) throws DAOException;
	
	
	public List<InventoryDrugCategory> findDrugCategory(String name) throws DAOException;
	
	public InventoryDrugCategory saveDrugCategory(InventoryDrugCategory drugCategory) throws DAOException;

	
	public int countListDrugCategory(String name)  throws DAOException;
	
	
	public InventoryDrugCategory getDrugCategoryById(Integer id) throws DAOException;
	
	
	public InventoryDrugCategory getDrugCategoryByName(String name) throws DAOException;
	
	public void deleteDrugCategory(InventoryDrugCategory drugCategory) throws DAOException;
	
	/**
	 * DrugFormulation
	 */
	 
	
	public List<InventoryDrugFormulation> listDrugFormulation(String name ,int min, int max) throws DAOException;
	
	
	public List<InventoryDrugFormulation> findDrugFormulation(String name) throws DAOException;
	
	public InventoryDrugFormulation saveDrugFormulation(InventoryDrugFormulation drugFormulation) throws DAOException;

	
	public int countListDrugFormulation(String name)  throws DAOException;
	
	
	public InventoryDrugFormulation getDrugFormulationById(Integer id) throws DAOException;
	
	public InventoryDrugFormulation getDrugFormulationByName(String name) throws DAOException;
	
	public InventoryDrugFormulation getDrugFormulation(String name ,String dozage) throws DAOException;
	
	public void deleteDrugFormulation(InventoryDrugFormulation drugFormulation) throws DAOException;
	
	/**
	 * DrugUnit
	 */
	 
	
	public List<InventoryDrugUnit> listDrugUnit(String name ,int min, int max) throws DAOException;
	
	
	public List<InventoryDrugUnit> findDrugUnit(String name) throws DAOException;
	
	public InventoryDrugUnit saveDrugUnit(InventoryDrugUnit drugUnit) throws DAOException;

	
	public int countListDrugUnit(String name)  throws DAOException;
	
	
	public InventoryDrugUnit getDrugUnitById(Integer id) throws DAOException;
	
	
	public InventoryDrugUnit getDrugUnitByName(String name) throws DAOException;
	
	public void deleteDrugUnit(InventoryDrugUnit drugUnit) throws DAOException;
	
	/**
	 * StoreDrug
	 */ 
	
	public List<InventoryStoreDrug> listStoreDrug(Integer storeId,Integer categoryId, String drugName,Integer reorderQty,int min, int max) throws DAOException;
	
	public int countStoreDrug(Integer storeId,Integer categoryId, String drugName,Integer reorderQty)  throws DAOException;
	
	public InventoryStoreDrug getStoreDrugById(Integer id) throws DAOException;
	
	public InventoryStoreDrug getStoreDrug(Integer storeId, Integer drugId,Integer formulationId) throws DAOException;
	
	public InventoryStoreDrug saveStoreDrug(InventoryStoreDrug storeDrug) throws DAOException;
	
	/**
	 * StoreDrugTransaction
	 */ 
	
	public List<InventoryStoreDrugTransaction> listStoreDrugTransaction(Integer transactionType,Integer storeId, String description, String fromDate, String toDate ,int min, int max) throws DAOException;
	
	public InventoryStoreDrugTransaction saveStoreDrugTransaction(InventoryStoreDrugTransaction storeTransaction) throws DAOException;

	public int countStoreDrugTransaction(Integer transactionType,Integer storeId, String description, String fromDate, String toDate)  throws DAOException;
	
	public InventoryStoreDrugTransaction getStoreDrugTransactionById(Integer id) throws DAOException;
	
	public InventoryStoreDrugTransaction getStoreDrugTransactionByParentId(Integer parentId) throws DAOException;
	
	/**
	 * StoreDrugTransactionDetail
	 */
	
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(Integer storeId,  Integer categoryId,String drugName,String formulationName, String fromDate, String toDate ,int min, int max) throws DAOException;
	
	public InventoryStoreDrugTransactionDetail saveStoreDrugTransactionDetail(InventoryStoreDrugTransactionDetail storeTransactionDetail) throws DAOException;

	public int countStoreDrugTransactionDetail(Integer storeId,  Integer categoryId,String drugName,String formulationName, String fromDate, String toDate )  throws DAOException;
	
	public InventoryStoreDrugTransactionDetail getStoreDrugTransactionDetailById(Integer id) throws DAOException;
	
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(Integer storeId,Integer drugId,Integer formulationId, boolean haveQuantity) throws DAOException;
	
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugAvaiable(Integer storeId,Collection<Integer> drugs,Collection<Integer> formulations) throws DAOException;
	
	public Integer sumCurrentQuantityDrugOfStore(Integer storeId,Integer drugId,Integer formulationId) throws DAOException;
	
	public List<InventoryStoreDrugTransactionDetail> listTransactionDetail(Integer transactionId) throws DAOException;
	
	public Integer countViewStockBalance(Integer storeId,Integer categoryId, String drugName , String fromDate , String toDate, boolean isExpiry) throws DAOException;
	
	public List<InventoryStoreDrugTransactionDetail> listViewStockBalance(Integer storeId,Integer categoryId, String drugName , String fromDate , String toDate, boolean isExpiry,int min, int max) throws DAOException;
	
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(Integer storeId,Integer drugId,Integer formulationId, Integer isExpriry) throws DAOException;
	
	public int checkExistDrugTransactionDetail(Integer drugId)  throws DAOException;
	/**
	 * InventoryStoreDrugIndent
	 */
	
	public  List<InventoryStoreDrugIndent> listStoreDrugIndent(Integer StoreId, String name, String fromDate, String toDate,int min, int max)  throws DAOException;
	
	public int countStoreDrugIndent(Integer StoreId, String name, String fromDate, String toDate)  throws DAOException;
	
	public List<InventoryStoreDrugIndent> listSubStoreIndent(Integer storeId, String name,Integer status, String fromDate, String toDate, int min, int max) throws DAOException;
	
	public int countSubStoreIndent(Integer storeId, String name,Integer status, String fromDate, String toDate)  throws DAOException;
	
	public List<InventoryStoreDrugIndent> listMainStoreIndent(Integer id, Integer mainStoreId,Integer subStoreId, String name,Integer status, String fromDate, String toDate, int min, int max) throws DAOException;
	
	public int countMainStoreIndent(Integer id, Integer mainStoreId,Integer subStoreId, String name,Integer status, String fromDate, String toDate)  throws DAOException;
	
	public InventoryStoreDrugIndent saveStoreDrugIndent(InventoryStoreDrugIndent storeDrugIndent) throws DAOException;
	
	public InventoryStoreDrugIndent getStoreDrugIndentById(Integer id) throws DAOException;
	
	/**
	 * InventoryStoreDrugIndentDetail
	 */
	
	public List<InventoryStoreDrugIndentDetail> listStoreDrugIndentDetail(Integer storeId, Integer categoryId, String indentName, String drugName, String fromDate, String toDate, int min, int max) throws DAOException;
	public int countStoreDrugIndentDetail(Integer storeId, Integer categoryId, String indentName, String drugName, String fromDate, String toDate) throws DAOException;
	public List<InventoryStoreDrugIndentDetail> listStoreDrugIndentDetail(Integer indentId) throws DAOException;
	public InventoryStoreDrugIndentDetail saveStoreDrugIndentDetail(InventoryStoreDrugIndentDetail storeDrugIndentDetail) throws DAOException;

	public InventoryStoreDrugIndentDetail getStoreDrugIndentDetailById(Integer id) throws DAOException;
	
	public int checkExistDrugIndentDetail(Integer drugId)  throws DAOException;
	
	
	/**
	 * InventoryStoreDrugPatient
	 */
	public List<InventoryStoreDrugPatient> listStoreDrugPatient(Integer storeId,String  name, String fromDate, String toDate, int min, int max) throws DAOException;
	
	public int countStoreDrugPatient(Integer storeId,String  name, String fromDate, String toDate)  throws DAOException;
	
	public InventoryStoreDrugPatient saveStoreDrugPatient(InventoryStoreDrugPatient bill) throws DAOException;
	
	public InventoryStoreDrugPatient getStoreDrugPatientById(Integer id) throws DAOException;
	
	/**
	 * InventoryStoreDrugPatientDetail
	 */
	public List<InventoryStoreDrugPatientDetail> listStoreDrugPatientDetail(Integer storeDrugPatientDetailId) throws DAOException;
	
	public InventoryStoreDrugPatientDetail saveStoreDrugPatientDetail(InventoryStoreDrugPatientDetail storeDrugPatientDetail) throws DAOException;

	public InventoryStoreDrugPatientDetail getStoreDrugPatientDetailById(Integer id) throws DAOException;
	
	//change from here
	
	/**
	 * StoreItem
	 */ 
	
	public List<InventoryStoreItem> listStoreItem(Integer storeId,Integer categoryId, String itemName,Integer reorderQty,int min, int max) throws DAOException;
	
	public int countStoreItem(Integer storeId,Integer categoryId, String itemName,Integer reorderQty)  throws DAOException;
	
	public InventoryStoreItem getStoreItemById(Integer id) throws DAOException;
	
	public InventoryStoreItem getStoreItem(Integer storeId, Integer itemId,Integer specificationId) throws DAOException;
	
	public InventoryStoreItem saveStoreItem(InventoryStoreItem StoreItem) throws DAOException;
	
	/**
	 * StoreItemTransaction
	 */ 
	
	public List<InventoryStoreItemTransaction> listStoreItemTransaction(Integer transactionType,Integer storeId, String description, String fromDate, String toDate ,int min, int max) throws DAOException;
	
	public InventoryStoreItemTransaction saveStoreItemTransaction(InventoryStoreItemTransaction storeTransaction) throws DAOException;

	public int countStoreItemTransaction(Integer transactionType,Integer storeId, String description, String fromDate, String toDate)  throws DAOException;
	
	public InventoryStoreItemTransaction getStoreItemTransactionById(Integer id) throws DAOException;
	
	public InventoryStoreItemTransaction getStoreItemTransactionByParentId(Integer parentId) throws DAOException;
	
	/**
	 * StoreItemTransactionDetail
	 */
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer storeId,  Integer categoryId,String itemName,String specificationName, String fromDate, String toDate ,int min, int max) throws DAOException;
	
	public InventoryStoreItemTransactionDetail saveStoreItemTransactionDetail(InventoryStoreItemTransactionDetail storeTransactionDetail) throws DAOException;

	public int countStoreItemTransactionDetail(Integer storeId,  Integer categoryId,String itemName,String specificationName, String fromDate, String toDate )  throws DAOException;
	
	public InventoryStoreItemTransactionDetail getStoreItemTransactionDetailById(Integer id) throws DAOException;
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer storeId,Integer itemId,Integer specificationId, boolean haveQuantity) throws DAOException;
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemAvaiable(Integer storeId,Collection<Integer> items,Collection<Integer> specifications) throws DAOException;
	
	public Integer sumStoreItemCurrentQuantity(Integer storeId,Integer itemId,Integer specificationId) throws DAOException;
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer transactionId) throws DAOException;
	
	public Integer countStoreItemViewStockBalance(Integer storeId,Integer categoryId, String itemName , String fromDate , String toDate) throws DAOException;
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemViewStockBalance(Integer storeId,Integer categoryId, String itemName , String fromDate , String toDate,int min, int max) throws DAOException;
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer storeId,Integer itemId,Integer specificationId ,int min, int max) throws DAOException;
	
	public int checkExistItemTransactionDetail(Integer itemId)  throws DAOException;
	
	
	/**
	 * InventoryStoreItemIndent
	 */
	
	public  List<InventoryStoreItemIndent> listStoreItemIndent(Integer StoreId, String name, String fromDate, String toDate,int min, int max)  throws DAOException;
	
	public int countStoreItemIndent(Integer StoreId, String name, String fromDate, String toDate)  throws DAOException;
	
	public List<InventoryStoreItemIndent> listSubStoreItemIndent(Integer storeId, String name,Integer status, String fromDate, String toDate, int min, int max) throws DAOException;
	
	public int countSubStoreItemIndent(Integer storeId, String name,Integer status, String fromDate, String toDate)  throws DAOException;
	
	public List<InventoryStoreItemIndent> listMainStoreItemIndent(Integer id, Integer mainStoreId,Integer subStoreId, String name,Integer status, String fromDate, String toDate, int min, int max) throws DAOException;
	
	public int countMainStoreItemIndent(Integer id, Integer mainStoreId,Integer subStoreId, String name,Integer status, String fromDate, String toDate)  throws DAOException;
	
	public InventoryStoreItemIndent saveStoreItemIndent(InventoryStoreItemIndent storeItemIndent) throws DAOException;
	
	public InventoryStoreItemIndent getStoreItemIndentById(Integer id) throws DAOException;
	
	/**
	 * InventoryStoreItemIndentDetail
	 */
	
	public List<InventoryStoreItemIndentDetail> listStoreItemIndentDetail(Integer storeId, Integer categoryId, String indentName, String itemName, String fromDate, String toDate, int min, int max) throws DAOException;
	
	public int countStoreItemIndentDetail(Integer storeId, Integer categoryId, String indentName, String itemName, String fromDate, String toDate) throws DAOException;
	
	public List<InventoryStoreItemIndentDetail> listStoreItemIndentDetail(Integer indentId) throws DAOException;
	
	public InventoryStoreItemIndentDetail saveStoreItemIndentDetail(InventoryStoreItemIndentDetail StoreItemIndentDetail) throws DAOException;

	public InventoryStoreItemIndentDetail getStoreItemIndentDetailById(Integer id) throws DAOException;
	
	public int checkExistItemIndentDetail(Integer itemId)  throws DAOException;
	
	
	/**
	 * InventoryStoreItemAccount
	 */
	public List<InventoryStoreItemAccount> listStoreItemAccount(Integer storeId,String  name, String fromDate, String toDate, int min, int max) throws DAOException;
	
	public int countStoreItemAccount(Integer storeId,String  name, String fromDate, String toDate)  throws DAOException;
	
	public InventoryStoreItemAccount saveStoreItemAccount(InventoryStoreItemAccount issue) throws DAOException;
	
	public InventoryStoreItemAccount getStoreItemAccountById(Integer id) throws DAOException;
	
	/**
	 * InventoryStoreItemAccountDetail
	 */
	public List<InventoryStoreItemAccountDetail> listStoreItemAccountDetail(Integer storeItemAccountDetailId) throws DAOException;
	
	public InventoryStoreItemAccountDetail saveStoreItemAccountDetail(InventoryStoreItemAccountDetail storeItemAccountDetail) throws DAOException;

	public InventoryStoreItemAccountDetail getStoreItemAccountDetailById(Integer id) throws DAOException;
	
	
	/**
	 * InventoryStoreDrugAccount
	 */
	public List<InventoryStoreDrugAccount> listStoreDrugAccount(Integer storeId,String  name, String fromDate, String toDate, int min, int max) throws DAOException;
	
	public int countStoreDrugAccount(Integer storeId,String  name, String fromDate, String toDate)  throws DAOException;
	
	public InventoryStoreDrugAccount saveStoreDrugAccount(InventoryStoreDrugAccount issue) throws DAOException;
	
	public InventoryStoreDrugAccount getStoreDrugAccountById(Integer id) throws DAOException;
	
	/**
	 * InventoryStoreDrugAccountDetail
	 */
	public List<InventoryStoreDrugAccountDetail> listStoreDrugAccountDetail(Integer storeDrugAccountDetailId) throws DAOException;
	
	public InventoryStoreDrugAccountDetail saveStoreDrugAccountDetail(InventoryStoreDrugAccountDetail storeDrugAccountDetail) throws DAOException;

	public InventoryStoreDrugAccountDetail getStoreDrugAccountDetailById(Integer id) throws DAOException;
	
	
}

