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
package org.openmrs.module.inventory;

import java.util.Collection;
import java.util.List;

import org.openmrs.Role;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
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
import org.springframework.transaction.annotation.Transactional;


/**
 *
 */
@Transactional
public interface InventoryService extends OpenmrsService{

	
	/**
	 * Store
	 */
	@Transactional(readOnly = true)
	public List<InventoryStore> listInventoryStore(int min, int max) throws APIException;
	
	@Transactional(readOnly = true)
	public List<InventoryStore> listAllInventoryStore() throws APIException;
	
	@Transactional(readOnly = true)
	public List<InventoryStore> listMainStore() throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStore saveStore(InventoryStore store) throws APIException;

	@Transactional(readOnly = true)
	public int countListStore()  throws APIException;
	
	@Transactional(readOnly = true)
	public InventoryStore getStoreById(Integer id) throws APIException;
	
	@Transactional(readOnly = true)
	public InventoryStore getStoreByName(String name) throws APIException;
	
	@Transactional(readOnly = true)
	public InventoryStore getStoreByRole(String role) throws APIException;
	
	@Transactional(readOnly = true)
	public InventoryStore getStoreByCollectionRole(List<Role> roles) throws APIException;
	
	@Transactional(readOnly=false)
	public void deleteStore(InventoryStore store) throws APIException;
	
	@Transactional(readOnly = true)
	public List<InventoryStore> listStoreByMainStore(Integer mainStoreid,boolean bothMainStore) throws APIException;
	
	/**
	 * ItemCategory
	 */
	 
	public List<InventoryItemCategory> listItemCategory(String name ,int min, int max) throws APIException;
	
	public List<InventoryItemCategory> findItemCategory(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryItemCategory saveItemCategory(InventoryItemCategory category) throws APIException;

	public int countListItemCategory(String name)  throws APIException;
	
	
	public InventoryItemCategory getItemCategoryById(Integer id) throws APIException;
	
	
	public InventoryItemCategory getItemCategoryByName(String name) throws APIException;
	
	
	public void deleteItemCategory(InventoryItemCategory category) throws APIException;
	
	/**
	 * ItemSubCategory
	 */
	 
	
	public List<InventoryItemSubCategory> listItemSubCategory(String name ,int min, int max) throws APIException;
	
	
	public List<InventoryItemSubCategory> findItemSubCategory(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryItemSubCategory saveItemSubCategory(InventoryItemSubCategory subCategory) throws APIException;

	
	public int countListItemSubCategory(String name)  throws APIException;
	
	
	public InventoryItemSubCategory getItemSubCategoryById(Integer id) throws APIException;
	
	
	public InventoryItemSubCategory getItemSubCategoryByName(Integer categoryId ,String name) throws APIException;
	
	public void deleteItemSubCategory(InventoryItemSubCategory subCategory) throws APIException;
	
	public List<InventoryItemSubCategory> listSubCatByCat(Integer categoryId) throws APIException;
	
	/**
	 * ItemSpecification
	 */
	 
	
	public List<InventoryItemSpecification> listItemSpecification(String name ,int min, int max) throws APIException;
	
	
	public List<InventoryItemSpecification> findItemSpecification(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryItemSpecification saveItemSpecification(InventoryItemSpecification specification) throws APIException;

	
	public int countListItemSpecification(String name)  throws APIException;
	
	
	public InventoryItemSpecification getItemSpecificationById(Integer id) throws APIException;
	
	
	public InventoryItemSpecification getItemSpecificationByName(String name) throws APIException;
	
	public void deleteItemSpecification(InventoryItemSpecification specification) throws APIException;
	
	/**
	 * ItemUnit
	 */
	 
	
	public List<InventoryItemUnit> listItemUnit(String name ,int min, int max) throws APIException;
	
	
	public List<InventoryItemUnit> findItemUnit(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryItemUnit saveItemUnit(InventoryItemUnit unit) throws APIException;

	
	public int countListItemUnit(String name)  throws APIException;
	
	
	public InventoryItemUnit getItemUnitById(Integer id) throws APIException;
	
	
	public InventoryItemUnit getItemUnitByName(String name) throws APIException;
	
	public void deleteItemUnit(InventoryItemUnit unit) throws APIException;
	
	/**
	 * Item
	 */
	 
	
	public List<InventoryItem> listItem(Integer categoryId, String name ,int min, int max) throws APIException;
	
	public List<InventoryItem> findItem(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryItem saveItem(InventoryItem item) throws APIException;

	public int countListItem(Integer categoryId, String name)  throws APIException;
	
	public InventoryItem getItemById(Integer id) throws APIException;
	
	public InventoryItem getItemByName(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public void deleteItem(InventoryItem item) throws APIException;
	
	public List<InventoryItem> findItem(Integer categoryId,String name) throws APIException;
	
	public int countItem(Integer categoryId, Integer unitId,  Integer subCategoryId, Integer specificationId)  throws APIException;
	
	
	/**
	 * Drug
	 */
	 
	
	public List<InventoryDrug> listDrug(Integer categoryId,  String name ,int min, int max) throws APIException;
	
	public List<InventoryDrug> findDrug(Integer categoryId,String name) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryDrug saveDrug(InventoryDrug drug) throws APIException;

	public int countListDrug(Integer categoryId, String name)  throws APIException;
	
	public InventoryDrug getDrugById(Integer id) throws APIException;
	
	public InventoryDrug getDrugByName(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public void deleteDrug(InventoryDrug drug) throws APIException;
	
	public int countListDrug(Integer categoryId, Integer unitId,  Integer formulationId)  throws APIException;
	
	
	/**
	 * DrugCategory
	 */
	 
	
	public List<InventoryDrugCategory> listDrugCategory(String name ,int min, int max) throws APIException;
	
	public List<InventoryDrugCategory> findDrugCategory(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryDrugCategory saveDrugCategory(InventoryDrugCategory drugCategory) throws APIException;

	public int countListDrugCategory(String name)  throws APIException;
	
	public InventoryDrugCategory getDrugCategoryById(Integer id) throws APIException;
	
	public InventoryDrugCategory getDrugCategoryByName(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public void deleteDrugCategory(InventoryDrugCategory drugCategory) throws APIException;
	
	/**
	 * DrugFormulation
	 */
	 
	
	public List<InventoryDrugFormulation> listDrugFormulation(String name ,int min, int max) throws APIException;
	
	public List<InventoryDrugFormulation> findDrugFormulation(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryDrugFormulation saveDrugFormulation(InventoryDrugFormulation drugFormulation) throws APIException;

	public int countListDrugFormulation(String name)  throws APIException;
	
	public InventoryDrugFormulation getDrugFormulationById(Integer id) throws APIException;
	
	public InventoryDrugFormulation getDrugFormulationByName(String name) throws APIException;
	
	public InventoryDrugFormulation getDrugFormulation(String name ,String dozage) throws APIException;
	
	@Transactional(readOnly=false)
	public void deleteDrugFormulation(InventoryDrugFormulation drugFormulation) throws APIException;
	
	/**
	 * DrugUnit
	 */
	public List<InventoryDrugUnit> listDrugUnit(String name ,int min, int max) throws APIException;
	
	public List<InventoryDrugUnit> findDrugUnit(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryDrugUnit saveDrugUnit(InventoryDrugUnit drugUnit) throws APIException;
	
	public int countListDrugUnit(String name)  throws APIException;
	
	public InventoryDrugUnit getDrugUnitById(Integer id) throws APIException;
	
	public InventoryDrugUnit getDrugUnitByName(String name) throws APIException;
	
	@Transactional(readOnly=false)
	public void deleteDrugUnit(InventoryDrugUnit drugUnit) throws APIException;
	
	/**
	 * StoreDrug
	 */ 
	
	public List<InventoryStoreDrug> listStoreDrug(Integer storeId,Integer categoryId, String drugName,Integer reorderQty,int min, int max) throws APIException;
	
	public int countStoreDrug(Integer storeId,Integer categoryId, String drugName,Integer reorderQty)  throws APIException;
	
	public InventoryStoreDrug getStoreDrugById(Integer id) throws APIException;
	
	public InventoryStoreDrug getStoreDrug(Integer storeId, Integer drugId,Integer formulationId) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreDrug saveStoreDrug(InventoryStoreDrug storeDrug) throws APIException;
	
	/**
	 * StoreDrugTransaction
	 */ 
	
	public List<InventoryStoreDrugTransaction> listStoreDrugTransaction(Integer transactionType,Integer storeId, String description, String fromDate, String toDate ,int min, int max) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreDrugTransaction saveStoreDrugTransaction(InventoryStoreDrugTransaction storeTransaction) throws APIException;

	public int countStoreDrugTransaction(Integer transactionType,Integer storeId, String description, String fromDate, String toDate)  throws APIException;
	
	public InventoryStoreDrugTransaction getStoreDrugTransactionById(Integer id) throws APIException;
	
	public InventoryStoreDrugTransaction getStoreDrugTransactionByParentId(Integer parentId) throws APIException;
	
	/**
	 * StoreDrugTransactionDetail
	 */
	
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(Integer storeId,  Integer categoryId,String drugName,String formulationName, String fromDate, String toDate ,int min, int max) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreDrugTransactionDetail saveStoreDrugTransactionDetail(InventoryStoreDrugTransactionDetail storeTransactionDetail) throws APIException;

	public int countStoreDrugTransactionDetail(Integer storeId,  Integer categoryId,String drugName,String formulationName, String fromDate, String toDate )  throws APIException;
	
	public InventoryStoreDrugTransactionDetail getStoreDrugTransactionDetailById(Integer id) throws APIException;
	
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(Integer storeId,Integer drugId,Integer formulationId, boolean haveQuantity) throws APIException;
	
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugAvaiable(Integer storeId,Collection<Integer> drugs,Collection<Integer> formulations) throws APIException;
	
	public List<InventoryStoreDrugTransactionDetail> listTransactionDetail(Integer transactionId) throws APIException;
	
	public Integer sumCurrentQuantityDrugOfStore(Integer storeId,Integer drugId,Integer formulationId) throws APIException;
	
	public Integer countViewStockBalance(Integer storeId,Integer categoryId, String drugName , String fromDate, String toDate, boolean isExpiry) throws APIException;
	
	public List<InventoryStoreDrugTransactionDetail> listViewStockBalance(Integer storeId,Integer categoryId, String drugName , String fromDate, String toDate,boolean isExpiry ,int min, int max) throws APIException;
	
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(Integer storeId,Integer drugId,Integer formulationId, Integer isExpiry) throws APIException;
	
	public int checkExistDrugTransactionDetail(Integer drugId)  throws APIException;
	
	/**
	 * InventoryStoreDrugIndent
	 */
	
	public  List<InventoryStoreDrugIndent> listStoreDrugIndent(Integer StoreId, String name, String fromDate, String toDate,int min, int max)  throws APIException;
	
	public int countStoreDrugIndent(Integer StoreId, String name, String fromDate, String toDate)  throws APIException;
	
	public List<InventoryStoreDrugIndent> listSubStoreIndent(Integer storeId, String name,Integer status, String fromDate, String toDate, int min, int max) throws APIException;
	
	public int countSubStoreIndent(Integer storeId, String name,Integer status, String fromDate, String toDate)  throws APIException;
	
	public List<InventoryStoreDrugIndent> listMainStoreIndent(Integer id, Integer mainStoreId,Integer subStoreId, String name,Integer status, String fromDate, String toDate, int min, int max) throws APIException;
	
	public int countMainStoreIndent(Integer id, Integer mainStoreId,Integer subStoreId, String name,Integer status, String fromDate, String toDate)  throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreDrugIndent saveStoreDrugIndent(InventoryStoreDrugIndent storeDrugIndent) throws APIException;
	
	public InventoryStoreDrugIndent getStoreDrugIndentById(Integer id) throws APIException;
	
	/**
	 * InventoryStoreDrugIndentDetail
	 */
	
	public List<InventoryStoreDrugIndentDetail> listStoreDrugIndentDetail(Integer storeId, Integer categoryId, String indentName, String drugName, String fromDate, String toDate, int min, int max) throws APIException;
	public List<InventoryStoreDrugIndentDetail> listStoreDrugIndentDetail(Integer indentId) throws APIException;
	public int countStoreDrugIndentDetail(Integer storeId, Integer categoryId, String indentName, String drugName, String fromDate, String toDate) throws APIException;
	@Transactional(readOnly=false)
	public InventoryStoreDrugIndentDetail saveStoreDrugIndentDetail(InventoryStoreDrugIndentDetail storeDrugIndentDetail) throws APIException;

	public InventoryStoreDrugIndentDetail getStoreDrugIndentDetailById(Integer id) throws APIException;
	
	public int checkExistDrugIndentDetail(Integer drugId)  throws APIException;
	
	/**
	 * InventoryStoreDrugPatient
	 */
	public List<InventoryStoreDrugPatient> listStoreDrugPatient(Integer storeId,String name, String fromDate, String toDate, int min, int max) throws APIException;
	
	public int countStoreDrugPatient(Integer storeId,String name, String fromDate, String toDate)  throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreDrugPatient saveStoreDrugPatient(InventoryStoreDrugPatient bill) throws APIException;
	
	public InventoryStoreDrugPatient getStoreDrugPatientById(Integer id) throws APIException;
	
	/**
	 * InventoryStoreDrugPatientDetail
	 */
	public List<InventoryStoreDrugPatientDetail> listStoreDrugPatientDetail(Integer storeDrugPatientDetailId) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreDrugPatientDetail saveStoreDrugPatientDetail(InventoryStoreDrugPatientDetail storeDrugPatientDetail) throws APIException;

	public InventoryStoreDrugPatientDetail getStoreDrugPatientDetailById(Integer id) throws APIException;
	
	//change code
	/**
	 * StoreItem
	 */ 
	
	public List<InventoryStoreItem> listStoreItem(Integer storeId,Integer categoryId, String itemName,Integer reorderQty,int min, int max) throws APIException;
	
	public int countStoreItem(Integer storeId,Integer categoryId, String itemName,Integer reorderQty)  throws APIException;
	
	public InventoryStoreItem getStoreItemById(Integer id) throws APIException;
	
	public InventoryStoreItem getStoreItem(Integer storeId, Integer itemId,Integer specificationId) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreItem saveStoreItem(InventoryStoreItem StoreItem) throws APIException;
	
	/**
	 * StoreItemTransaction
	 */ 
	
	public List<InventoryStoreItemTransaction> listStoreItemTransaction(Integer transactionType,Integer storeId, String description, String fromDate, String toDate ,int min, int max) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreItemTransaction saveStoreItemTransaction(InventoryStoreItemTransaction storeTransaction) throws APIException;

	public int countStoreItemTransaction(Integer transactionType,Integer storeId, String description, String fromDate, String toDate)  throws APIException;
	
	public InventoryStoreItemTransaction getStoreItemTransactionById(Integer id) throws APIException;
	
	public InventoryStoreItemTransaction getStoreItemTransactionByParentId(Integer parentId) throws APIException;
	
	/**
	 * StoreItemTransactionDetail
	 */
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer storeId,  Integer categoryId,String itemName,String specificationName, String fromDate, String toDate ,int min, int max) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreItemTransactionDetail saveStoreItemTransactionDetail(InventoryStoreItemTransactionDetail storeTransactionDetail) throws APIException;

	public int countStoreItemTransactionDetail(Integer storeId,  Integer categoryId,String itemName,String specificationName, String fromDate, String toDate )  throws APIException;
	
	public InventoryStoreItemTransactionDetail getStoreItemTransactionDetailById(Integer id) throws APIException;
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer storeId,Integer itemId,Integer specificationId, boolean haveQuantity) throws APIException;
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemAvaiable(Integer storeId,Collection<Integer> items,Collection<Integer> specifications) throws APIException;
	
	public Integer sumStoreItemCurrentQuantity(Integer storeId,Integer itemId,Integer specificationId) throws APIException;
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer transactionId) throws APIException;
	
	public Integer countStoreItemViewStockBalance(Integer storeId,Integer categoryId, String itemName ,String fromDate, String toDate) throws APIException;
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemViewStockBalance(Integer storeId,Integer categoryId, String itemName , String fromDate, String toDate,int min, int max) throws APIException;
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer storeId,Integer itemId,Integer specificationId ,int min, int max) throws APIException;
	
	public int checkExistItemTransactionDetail(Integer itemId)  throws APIException;
	
	/**
	 * InventoryStoreItemIndent
	 */
	
	public  List<InventoryStoreItemIndent> listStoreItemIndent(Integer StoreId, String name, String fromDate, String toDate,int min, int max)  throws APIException;
	
	public int countStoreItemIndent(Integer StoreId, String name, String fromDate, String toDate)  throws APIException;
	
	public List<InventoryStoreItemIndent> listSubStoreItemIndent(Integer storeId, String name,Integer status, String fromDate, String toDate, int min, int max) throws APIException;
	
	public int countSubStoreItemIndent(Integer storeId, String name,Integer status, String fromDate, String toDate)  throws APIException;
	
	public List<InventoryStoreItemIndent> listMainStoreItemIndent(Integer id,Integer mainStoreId,Integer subStoreId, String name,Integer status, String fromDate, String toDate, int min, int max) throws APIException;
	
	public int countMainStoreItemIndent(Integer id,Integer mainStoreId,Integer subStoreId, String name,Integer status, String fromDate, String toDate)  throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreItemIndent saveStoreItemIndent(InventoryStoreItemIndent storeItemIndent) throws APIException;
	
	public InventoryStoreItemIndent getStoreItemIndentById(Integer id) throws APIException;
	
	/**
	 * InventoryStoreItemIndentDetail
	 */
	
	public List<InventoryStoreItemIndentDetail> listStoreItemIndentDetail(Integer storeId, Integer categoryId, String indentName, String itemName, String fromDate, String toDate, int min, int max) throws APIException;
	
	public int countStoreItemIndentDetail(Integer storeId, Integer categoryId, String indentName, String itemName, String fromDate, String toDate) throws APIException;
	
	public List<InventoryStoreItemIndentDetail> listStoreItemIndentDetail(Integer indentId) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreItemIndentDetail saveStoreItemIndentDetail(InventoryStoreItemIndentDetail storeItemIndentDetail) throws APIException;

	public InventoryStoreItemIndentDetail getStoreItemIndentDetailById(Integer id) throws APIException;
	
	public int checkExistItemIndentDetail(Integer itemId)  throws APIException;
	
	
	/**
	 * InventoryStoreItemAccount
	 */
	public List<InventoryStoreItemAccount> listStoreItemAccount(Integer storeId,String  name, String fromDate, String toDate, int min, int max) throws APIException;
	
	public int countStoreItemAccount(Integer storeId,String  name, String fromDate, String toDate)  throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreItemAccount saveStoreItemAccount(InventoryStoreItemAccount issue) throws APIException;
	
	public InventoryStoreItemAccount getStoreItemAccountById(Integer id) throws APIException;
	
	/**
	 * InventoryStoreItemAccountDetail
	 */
	public List<InventoryStoreItemAccountDetail> listStoreItemAccountDetail(Integer storeItemAccountDetailId) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreItemAccountDetail saveStoreItemAccountDetail(InventoryStoreItemAccountDetail storeItemAccountDetail) throws APIException;

	public InventoryStoreItemAccountDetail getStoreItemAccountDetailById(Integer id) throws APIException;
	
	
	/**
	 * InventoryStoreDrugAccount
	 */
	public List<InventoryStoreDrugAccount> listStoreDrugAccount(Integer storeId,String  name, String fromDate, String toDate, int min, int max) throws APIException;
	
	public int countStoreDrugAccount(Integer storeId,String  name, String fromDate, String toDate)  throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreDrugAccount saveStoreDrugAccount(InventoryStoreDrugAccount issue) throws APIException;
	
	public InventoryStoreDrugAccount getStoreDrugAccountById(Integer id) throws APIException;
	
	/**
	 * InventoryStoreDrugAccountDetail
	 */
	public List<InventoryStoreDrugAccountDetail> listStoreDrugAccountDetail(Integer storeDrugAccountDetailId) throws APIException;
	
	@Transactional(readOnly=false)
	public InventoryStoreDrugAccountDetail saveStoreDrugAccountDetail(InventoryStoreDrugAccountDetail storeDrugAccountDetail) throws APIException;

	public InventoryStoreDrugAccountDetail getStoreDrugAccountDetailById(Integer id) throws APIException;
	
}
