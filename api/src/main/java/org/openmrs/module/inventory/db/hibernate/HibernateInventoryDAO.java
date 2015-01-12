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
package org.openmrs.module.inventory.db.hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.openmrs.Encounter;
import org.openmrs.Role;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.hospitalcore.model.InventoryDrug;
import org.openmrs.module.hospitalcore.model.InventoryDrugCategory;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.hospitalcore.model.InventoryDrugUnit;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugIndent;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatient;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatientDetail;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransaction;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.hospitalcore.model.OpdDrugOrder;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.module.hospitalcore.util.ActionValue;
import org.openmrs.module.inventory.InventoryConstants;
import org.openmrs.module.inventory.db.InventoryDAO;
import org.openmrs.module.inventory.model.InventoryItem;
import org.openmrs.module.inventory.model.InventoryItemCategory;
import org.openmrs.module.inventory.model.InventoryItemSpecification;
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.openmrs.module.inventory.model.InventoryItemUnit;
import org.openmrs.module.inventory.model.InventoryStoreDrug;
import org.openmrs.module.inventory.model.InventoryStoreDrugAccount;
import org.openmrs.module.inventory.model.InventoryStoreDrugAccountDetail;
import org.openmrs.module.inventory.model.InventoryStoreDrugIndentDetail;
import org.openmrs.module.inventory.model.InventoryStoreItem;
import org.openmrs.module.inventory.model.InventoryStoreItemAccount;
import org.openmrs.module.inventory.model.InventoryStoreItemAccountDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemIndent;
import org.openmrs.module.inventory.model.InventoryStoreItemIndentDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemPatient;
import org.openmrs.module.inventory.model.InventoryStoreItemPatientDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemTransaction;
import org.openmrs.module.inventory.model.InventoryStoreItemTransactionDetail;

/**
 * Hibernate specific Idcards database methods
 */
public class HibernateInventoryDAO implements InventoryDAO {

	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	SimpleDateFormat formatterExt = new SimpleDateFormat("dd/MM/yyyy");

	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;

	/**
	 * Set session factory
	 *
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<InventoryStore> listInventoryStore(int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStore.class);
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryStore> l = criteria.list();

		return l;
	}

	@SuppressWarnings("unchecked")
	public List<InventoryStore> listMainStore() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStore.class);
		criteria.add(Restrictions.isNull("parent"));
		List<InventoryStore> l = criteria.list();
		// System.out.println("listMainStore>>l: "+l);
		return l;
	}

	public InventoryStore saveStore(InventoryStore store) throws DAOException {
		return (InventoryStore) sessionFactory.getCurrentSession().merge(store);
	}

	public int countListStore() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStore.class);
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStore getStoreById(Integer id) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStore.class);
		criteria.add(Restrictions.eq("id", id));
		InventoryStore store = (InventoryStore) criteria.uniqueResult();
		return store;
	}

	public InventoryStore getStoreByName(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStore.class);
		criteria.add(Restrictions.eq("name", name));
		InventoryStore store = (InventoryStore) criteria.uniqueResult();
		return store;
	}

	public InventoryStore getStoreByRole(String role) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStore.class);
		criteria.add(Restrictions.eq("role", role));
		criteria.setMaxResults(1);
		List<InventoryStore> list = criteria.list();
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	public InventoryStore getStoreByCollectionRole(List<Role> roles)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStore.class);
		criteria.add(Restrictions.in("role", roles));
		criteria.setMaxResults(1);
		List<InventoryStore> list = criteria.list();
		return CollectionUtils.isEmpty(list) ? null : list.get(0);

	}

	public void deleteStore(InventoryStore store) throws DAOException {
		sessionFactory.getCurrentSession().delete(store);
	}

	@SuppressWarnings("unchecked")
	public List<InventoryStore> listAllInventoryStore() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStore.class);
		List<InventoryStore> list = criteria.list();
		return list;
	}

	public List<InventoryStore> listStoreByMainStore(Integer mainStoreid,
			boolean bothMainStore) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStore.class, "store").createAlias("parentStores", "parent");

		if (bothMainStore) {
			criteria.add(Restrictions.or(
					Restrictions.eq("parent.id", mainStoreid),
					Restrictions.eq("store.id", mainStoreid)));
		} else {
			criteria.add(Restrictions.eq("parent.id", mainStoreid));
		}
		List<InventoryStore> l = criteria.list();
		return l;
	}

	/**
	 * ItemCategory
	 */

	public List<InventoryItemCategory> listItemCategory(String name, int min,
			int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemCategory.class, "itemCategory");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("itemCategory.name", "%" + name
					+ "%"));
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryItemCategory> l = criteria.list();

		return l;

	}

	public List<InventoryItemCategory> findItemCategory(String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemCategory.class, "itemCategory");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("itemCategory.name", "%" + name
					+ "%"));
		}
		List<InventoryItemCategory> l = criteria.list();

		return l;
	}

	public InventoryItemCategory saveItemCategory(InventoryItemCategory category)
			throws DAOException {
		return (InventoryItemCategory) sessionFactory.getCurrentSession()
				.merge(category);
	}

	public int countListItemCategory(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemCategory.class, "itemCategory");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("itemCategory.name", "%" + name
					+ "%"));
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryItemCategory getItemCategoryById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryItemCategory.class, "itemCategory")
				.add(Restrictions.eq("itemCategory.id", id));
		return (InventoryItemCategory) criteria.uniqueResult();
	}

	public InventoryItemCategory getItemCategoryByName(String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryItemCategory.class, "itemCategory")
				.add(Restrictions.eq("itemCategory.name", name));
		return (InventoryItemCategory) criteria.uniqueResult();
	}

	public void deleteItemCategory(InventoryItemCategory category)
			throws DAOException {
		sessionFactory.getCurrentSession().delete(category);
	}

	/**
	 * ItemSubCategory
	 */

	public List<InventoryItemSubCategory> listItemSubCategory(String name,
			int min, int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemSubCategory.class, "itemSubCategory");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("itemSubCategory.name", "%" + name
					+ "%"));
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryItemSubCategory> l = criteria.list();

		return l;
	}

	public List<InventoryItemSubCategory> findItemSubCategory(String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemSubCategory.class, "itemSubCategory");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("itemSubCategory.name", "%" + name
					+ "%"));
		}
		List<InventoryItemSubCategory> l = criteria.list();

		return l;
	}

	public List<InventoryItemSubCategory> listSubCatByCat(Integer categoryId)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemSubCategory.class, "itemSubCategory");
		criteria.add(Restrictions.eq("itemSubCategory.category.id", categoryId));
		List<InventoryItemSubCategory> l = criteria.list();

		return l;
	}

	public InventoryItemSubCategory saveItemSubCategory(
			InventoryItemSubCategory subCategory) throws DAOException {
		return (InventoryItemSubCategory) sessionFactory.getCurrentSession()
				.merge(subCategory);
	}

	public int countListItemSubCategory(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemSubCategory.class, "itemSubCategory");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("itemSubCategory.name", "%" + name
					+ "%"));
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryItemSubCategory getItemSubCategoryById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryItemSubCategory.class,
						"itemSubCategory")
				.add(Restrictions.eq("itemSubCategory.id", id));
		return (InventoryItemSubCategory) criteria.uniqueResult();
	}

	public InventoryItemSubCategory getItemSubCategoryByName(
			Integer categoryId, String name) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryItemSubCategory.class,
						"itemSubCategory")
				.add(Restrictions.eq("itemSubCategory.name", name))
				.add(Restrictions.eq("itemSubCategory.category.id", categoryId));
		return (InventoryItemSubCategory) criteria.uniqueResult();
	}

	public void deleteItemSubCategory(InventoryItemSubCategory subCategory)
			throws DAOException {
		sessionFactory.getCurrentSession().delete(subCategory);

	}

	/**
	 * ItemSpecification
	 */

	public List<InventoryItemSpecification> listItemSpecification(String name,
			int min, int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemSpecification.class, "specification");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("specification.name", "%" + name
					+ "%"));
		}
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryItemSpecification> l = criteria.list();

		return l;
	}

	public List<InventoryItemSpecification> findItemSpecification(String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemSpecification.class, "specification");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("specification.name", "%" + name
					+ "%"));
		}
		List<InventoryItemSpecification> l = criteria.list();

		return l;
	}

	public InventoryItemSpecification saveItemSpecification(
			InventoryItemSpecification specification) throws DAOException {
		return (InventoryItemSpecification) sessionFactory.getCurrentSession()
				.merge(specification);
	}

	public int countListItemSpecification(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemSpecification.class, "specification");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("specification.name", "%" + name
					+ "%"));
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryItemSpecification getItemSpecificationById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemSpecification.class, "specification");
		criteria.add(Restrictions.eq("specification.id", id));
		return (InventoryItemSpecification) criteria.uniqueResult();

	}

	public InventoryItemSpecification getItemSpecificationByName(String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemSpecification.class, "specification");
		criteria.add(Restrictions.eq("specification.name", name));
		return (InventoryItemSpecification) criteria.uniqueResult();
	}

	public void deleteItemSpecification(InventoryItemSpecification specification)
			throws DAOException {
		sessionFactory.getCurrentSession().delete(specification);
	}

	/**
	 * ItemUnit
	 */

	public List<InventoryItemUnit> listItemUnit(String name, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemUnit.class, "itemUnit");
		if (StringUtils.isNotBlank(name)) {
			criteria.add(Restrictions.like("itemUnit.name", "%" + name + "%"));
		}
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryItemUnit> l = criteria.list();

		return l;
	}

	public List<InventoryItemUnit> findItemUnit(String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemUnit.class, "itemUnit");
		if (StringUtils.isNotBlank(name)) {
			criteria.add(Restrictions.like("itemUnit.name", "%" + name + "%"));
		}
		List<InventoryItemUnit> l = criteria.list();

		return l;
	}

	public InventoryItemUnit saveItemUnit(InventoryItemUnit unit)
			throws DAOException {
		return (InventoryItemUnit) sessionFactory.getCurrentSession().merge(
				unit);
	}

	public int countListItemUnit(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemUnit.class, "itemUnit");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("itemUnit.name", "%" + name + "%"));
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryItemUnit getItemUnitById(Integer id) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemUnit.class, "itemUnit");
		criteria.add(Restrictions.eq("itemUnit.id", id));
		return (InventoryItemUnit) criteria.uniqueResult();
	}

	public InventoryItemUnit getItemUnitByName(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItemUnit.class, "itemUnit");
		criteria.add(Restrictions.eq("itemUnit.name", name));
		return (InventoryItemUnit) criteria.uniqueResult();
	}

	public void deleteItemUnit(InventoryItemUnit unit) throws DAOException {
		sessionFactory.getCurrentSession().delete(unit);
	}

	/**
	 * Item
	 */

	public List<InventoryItem> listItem(Integer categoryId, String name,
			int min, int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItem.class, "item");
		if (StringUtils.isNotBlank(name)) {
			criteria.add(Restrictions.like("item.name", "%" + name + "%"));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("item.subCategory.id", categoryId));
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryItem> l = criteria.list();

		return l;
	}

	public int countItem(Integer categoryId, Integer unitId,
			Integer subCategoryId, Integer specificationId) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItem.class, "item");
		if (categoryId != null && categoryId > 0) {
			criteria.add(Restrictions.eq("item.category.id", categoryId));
		}
		if (subCategoryId != null && subCategoryId > 0) {
			criteria.add(Restrictions.eq("item.subCategory.id", subCategoryId));
		}
		if (unitId != null && unitId > 0) {
			criteria.add(Restrictions.eq("item.unit.id", unitId));
		}
		if (specificationId != null && specificationId > 0) {
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.createCriteria("item.specifications", Criteria.LEFT_JOIN)
					.add(Restrictions.eq("id", specificationId));
		}

		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public List<InventoryItem> findItem(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItem.class, "item");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("item.name", "%" + name + "%"));
		}
		List<InventoryItem> l = criteria.list();

		return l;
	}

	public List<InventoryItem> findItem(Integer categoryId, String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItem.class, "item");
		if (categoryId != null) {
			criteria.add(Restrictions.eq("item.subCategory.id", categoryId));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("item.name", "%" + name + "%"));
		}
		List<InventoryItem> l = criteria.list();

		return l;
	}

	public InventoryItem saveItem(InventoryItem item) throws DAOException {
		return (InventoryItem) sessionFactory.getCurrentSession().merge(item);
	}

	public int countListItem(Integer categoryId, String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryItem.class, "item");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("item.name", "%" + name + "%"));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("item.subCategory.id", categoryId));
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryItem getItemById(Integer id) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryItem.class, "item")
				.add(Restrictions.eq("item.id", id));
		return (InventoryItem) criteria.uniqueResult();
	}

	public InventoryItem getItemByName(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryItem.class, "item")
				.add(Restrictions.eq("item.name", name));
		return (InventoryItem) criteria.uniqueResult();
	}

	public void deleteItem(InventoryItem item) throws DAOException {
		sessionFactory.getCurrentSession().delete(item);
	}

	/**
	 * Drug
	 */

	public List<InventoryDrug> listDrug(Integer categoryId, String name,
			int min, int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrug.class, "drug");
		if (StringUtils.isNotBlank(name)) {
			criteria.add(Restrictions.like("drug.name", "%" + name + "%"));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("drug.category.id", categoryId));
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryDrug> l = criteria.list();

		return l;
	}

	public List<InventoryDrug> findDrug(Integer categoryId, String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrug.class, "drug");
		if (categoryId != null) {
			criteria.add(Restrictions.eq("drug.category.id", categoryId));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("drug.name", "%" + name + "%"));
		}
		List<InventoryDrug> l = criteria.list();

		return l;
	}

	public InventoryDrug saveDrug(InventoryDrug drug) throws DAOException {
		return (InventoryDrug) sessionFactory.getCurrentSession().merge(drug);
	}

	public int countListDrug(Integer categoryId, String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrug.class, "drug");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("drug.name", "%" + name + "%"));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("drug.category.id", categoryId));
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public int countListDrug(Integer categoryId, Integer unitId,
			Integer formulationId) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrug.class, "drug");
		if (categoryId != null && categoryId > 0) {
			criteria.add(Restrictions.eq("drug.category.id", categoryId));
		}
		if (unitId != null && unitId > 0) {
			criteria.add(Restrictions.eq("drug.unit.id", unitId));
		}

		if (formulationId != null && formulationId > 0) {
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.createCriteria("drug.formulations", Criteria.LEFT_JOIN)
					.add(Restrictions.eq("id", formulationId));
		}

		// .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryDrug getDrugById(Integer id) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryDrug.class, "drug")
				.add(Restrictions.eq("drug.id", id));
		return (InventoryDrug) criteria.uniqueResult();
	}

	public InventoryDrug getDrugByName(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryDrug.class, "drug")
				.add(Restrictions.eq("drug.name", name));
		return (InventoryDrug) criteria.uniqueResult();
	}

	public void deleteDrug(InventoryDrug drug) throws DAOException {
		sessionFactory.getCurrentSession().delete(drug);
	}

	/**
	 * Implement for InventoryDAO interface - Get All Drugs (Order with
	 * drug.name)
	 *
	 * @support feature#174
	 * @author Thai Chuong
	 * @date <dd/mm/yyyy>08/05/2012
	 * @return List <InventoryDrug>
	 * @throws DAOException
	 */
	public List<InventoryDrug> getAllDrug() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryDrug.class, "drug")
				.addOrder(Order.asc("drug.name"));

		List<InventoryDrug> l = criteria.list();

		return l;
	}

	/**
	 * DrugCategory
	 */

	public List<InventoryDrugCategory> listDrugCategory(String name, int min,
			int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrugCategory.class, "drugCategory");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("drugCategory.name", "%" + name
					+ "%"));
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryDrugCategory> l = criteria.list();

		return l;
	}

	public List<InventoryDrugCategory> findDrugCategory(String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrugCategory.class, "drugCategory");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("drugCategory.name", "%" + name
					+ "%"));
		}
		List<InventoryDrugCategory> l = criteria.list();

		return l;
	}

	public InventoryDrugCategory saveDrugCategory(
			InventoryDrugCategory drugCategory) throws DAOException {
		return (InventoryDrugCategory) sessionFactory.getCurrentSession()
				.merge(drugCategory);
	}

	public int countListDrugCategory(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrugCategory.class, "drugCategory");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("drugCategory.name", "%" + name
					+ "%"));
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryDrugCategory getDrugCategoryById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryDrugCategory.class, "drugCategory")
				.add(Restrictions.eq("drugCategory.id", id));
		return (InventoryDrugCategory) criteria.uniqueResult();
	}

	public InventoryDrugCategory getDrugCategoryByName(String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryDrugCategory.class, "drugCategory")
				.add(Restrictions.eq("drugCategory.name", name));
		return (InventoryDrugCategory) criteria.uniqueResult();
	}

	public void deleteDrugCategory(InventoryDrugCategory drugCategory)
			throws DAOException {
		sessionFactory.getCurrentSession().delete(drugCategory);
	}

	/**
	 * DrugFormulation
	 */

	public List<InventoryDrugFormulation> listDrugFormulation(String name,
			int min, int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrugFormulation.class, "drugFormulation");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("drugFormulation.name", "%" + name
					+ "%"));
		}
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryDrugFormulation> l = criteria.list();

		return l;
	}

	public List<InventoryDrugFormulation> findDrugFormulation(String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrugFormulation.class, "drugFormulation");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.eq("drugFormulation.name", "%" + name
					+ "%"));
		}
		List<InventoryDrugFormulation> l = criteria.list();

		return l;
	}

	public InventoryDrugFormulation saveDrugFormulation(
			InventoryDrugFormulation drugFormulation) throws DAOException {
		return (InventoryDrugFormulation) sessionFactory.getCurrentSession()
				.merge(drugFormulation);
	}

	public int countListDrugFormulation(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrugFormulation.class, "drugFormulation");
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("drugFormulation.name", "%" + name
					+ "%"));
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryDrugFormulation getDrugFormulationById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryDrugFormulation.class,
						"drugFormulation")
				.add(Restrictions.eq("drugFormulation.id", id));
		return (InventoryDrugFormulation) criteria.uniqueResult();
	}

	public InventoryDrugFormulation getDrugFormulationByName(String name)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryDrugFormulation.class,
						"drugFormulation")
				.add(Restrictions.eq("drugFormulation.name", name));
		return (InventoryDrugFormulation) criteria.uniqueResult();
	}

	public InventoryDrugFormulation getDrugFormulation(String name,
			String dozage) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryDrugFormulation.class,
						"drugFormulation")
				.add(Restrictions.eq("drugFormulation.dozage", dozage))
				.add(Restrictions.eq("drugFormulation.name", name));
		return (InventoryDrugFormulation) criteria.uniqueResult();
	}

	public void deleteDrugFormulation(InventoryDrugFormulation drugFormulation)
			throws DAOException {
		sessionFactory.getCurrentSession().delete(drugFormulation);
	}

	/**
	 * DrugUnit
	 */

	public List<InventoryDrugUnit> listDrugUnit(String name, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrugUnit.class, "drugUnit");
		if (StringUtils.isNotBlank(name)) {
			criteria.add(Restrictions.like("drugUnit.name", "%" + name + "%"));
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryDrugUnit> l = criteria.list();
		return l;
	}

	public List<InventoryDrugUnit> findDrugUnit(String name)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrugUnit.class, "drugUnit");
		if (StringUtils.isNotBlank(name)) {
			criteria.add(Restrictions.like("drugUnit.name", "%" + name + "%"));
		}
		List<InventoryDrugUnit> l = criteria.list();

		return l;
	}

	public InventoryDrugUnit saveDrugUnit(InventoryDrugUnit drugUnit)
			throws DAOException {
		return (InventoryDrugUnit) sessionFactory.getCurrentSession().merge(
				drugUnit);
	}

	public int countListDrugUnit(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrugUnit.class, "drugUnit");
		if (StringUtils.isNotBlank(name)) {
			criteria.add(Restrictions.like("drugUnit.name", "%" + name + "%"));
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryDrugUnit getDrugUnitById(Integer id) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrugUnit.class, "drugUnit");
		criteria.add(Restrictions.eq("drugUnit.id", id));
		return (InventoryDrugUnit) criteria.uniqueResult();
	}

	public InventoryDrugUnit getDrugUnitByName(String name) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryDrugUnit.class, "drugUnit");
		criteria.add(Restrictions.eq("drugUnit.name", name));
		return (InventoryDrugUnit) criteria.uniqueResult();
	}

	/**
	 * StoreDrug
	 */

	public List<InventoryStoreDrug> listStoreDrug(Integer storeId,
			Integer categoryId, String drugName, Integer reOrderQty, int min,
			int max) throws DAOException {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrug.class, "storeDrug")
				.createAlias("storeDrug.drug", "drug")
				.createAlias("storeDrug.formulation", "formulation")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (storeId != null) {
			criteria.add(Restrictions.eq("storeDrug.store.id", storeId));
		}
		if (!StringUtils.isBlank(drugName)) {
			criteria.add(Restrictions.like("drug.name", "%" + drugName + "%"));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("drug.category.id", categoryId));
		}
		if (reOrderQty != null) {
			criteria.add(Restrictions.eq("storeDrug.reorderQty", reOrderQty));
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryStoreDrug> l = criteria.list();

		return l;

	}

	public int countStoreDrug(Integer storeId, Integer categoryId,
			String drugName, Integer reOrderQty) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrug.class, "storeDrug")
				.createAlias("storeDrug.drug", "drug")
				.createAlias("storeDrug.formulation", "formulation")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (storeId != null) {
			criteria.add(Restrictions.eq("storeDrug.store.id", storeId));
		}
		if (!StringUtils.isBlank(drugName)) {
			criteria.add(Restrictions.like("drug.name", "%" + drugName + "%"));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("drug.category.id", categoryId));
		}
		if (reOrderQty != null) {
			criteria.add(Restrictions.eq("storeDrug.reorderQty", reOrderQty));
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreDrug getStoreDrugById(Integer id) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreDrug.class, "storeDrug");
		criteria.add(Restrictions.eq("storeDrug.id", id));
		return (InventoryStoreDrug) criteria.uniqueResult();
	}

	public InventoryStoreDrug getStoreDrug(Integer storeId, Integer drugId,
			Integer formulationId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrug.class, "storeDrug")
				.add(Restrictions.eq("storeDrug.store.id", storeId))
				.add(Restrictions.eq("storeDrug.drug.id", drugId))
				.add(Restrictions.eq("storeDrug.formulation.id", formulationId));
		return (InventoryStoreDrug) criteria.uniqueResult();
	}

	public InventoryStoreDrug saveStoreDrug(InventoryStoreDrug storeDrug)
			throws DAOException {
		return (InventoryStoreDrug) sessionFactory.getCurrentSession().merge(
				storeDrug);
	}

	/**
	 * StoreDrugTransaction
	 */

	public List<InventoryStoreDrugTransaction> listStoreDrugTransaction(
			Integer transactionType, Integer storeId, String description,
			String fromDate, String toDate, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreDrugTransaction.class);
		if (storeId != null) {
			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(description)) {
			criteria.add(Restrictions.like("description", "%" + description
					+ "%"));
		}
		if (transactionType != null) {
			criteria.add(Restrictions.eq("typeTransaction", transactionType));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				// System.out.println("Error convert date: "+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				// System.out.println("Error convert date: "+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		criteria.addOrder(Order.desc("createdOn"));
		List<InventoryStoreDrugTransaction> l = criteria.list();

		return l;
	}

	public List<InventoryStoreDrugTransaction> listStoreDrugTransaction(
			Integer transactionType, Integer storeId, String description,
			String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreDrugTransaction.class);
		if (storeId != null) {
			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(description)) {
			criteria.add(Restrictions.like("description", "%" + description
					+ "%"));
		}
		if (transactionType != null) {
			criteria.add(Restrictions.eq("typeTransaction", transactionType));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		}
		List<InventoryStoreDrugTransaction> l = criteria.list();

		return l;
	}

	public InventoryStoreDrugTransaction saveStoreDrugTransaction(
			InventoryStoreDrugTransaction storeTransaction) throws DAOException {
		return (InventoryStoreDrugTransaction) sessionFactory
				.getCurrentSession().merge(storeTransaction);
	}

	public int countStoreDrugTransaction(Integer transactionType,
			Integer storeId, String description, String fromDate, String toDate)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreDrugTransaction.class);
		if (storeId != null) {
			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(description)) {
			criteria.add(Restrictions.like("description", "%" + description
					+ "%"));
		}
		if (transactionType != null) {
			criteria.add(Restrictions.eq("typeTransaction", transactionType));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreDrugTransaction getStoreDrugTransactionById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreDrugTransaction.class, "storeDrugTransaction");
		criteria.add(Restrictions.eq("storeDrugTransaction.id", id));
		return (InventoryStoreDrugTransaction) criteria.uniqueResult();
	}

	public InventoryStoreDrugTransaction getStoreDrugTransactionByParentId(
			Integer parentId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugTransaction.class,
						"storeDrugTransaction")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("storeDrugTransaction.parent", "parent");
		criteria.add(Restrictions.eq("parent.id", parentId));
		return (InventoryStoreDrugTransaction) criteria.uniqueResult();
	}

	/**
	 * StoreDrugTransactionDetail
	 */

	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(
			Integer storeId, Integer categoryId, String drugName,
			String formulationName, String fromDate, String toDate, int min,
			int max) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugTransactionDetail.class,
						"transactionDetail")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("transactionDetail.transaction", "transaction")
				.createAlias("transactionDetail.drug", "drug")
				.createAlias("transactionDetail.formulation", "formulation");
		criteria.add(Restrictions.eq("transaction.typeTransaction",
				ActionValue.TRANSACTION[0]));
		if (storeId != null) {
			criteria.add(Restrictions.eq("transaction.store.id", storeId));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("drug.category.id", categoryId));
		}
		if (!StringUtils.isBlank(drugName)) {
			criteria.add(Restrictions.like("drug.name", "%" + drugName + "%"));
		}
		if (!StringUtils.isBlank(formulationName)) {
			criteria.add(Restrictions.like("formulation.name", "%"
					+ formulationName + "%"));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType1>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType2>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType3>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryStoreDrugTransactionDetail> l = criteria.list();

		return l;
	}

	@Override
	public List<InventoryStoreDrugTransactionDetail> listTransactionDetail(
			Integer transactionId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugTransactionDetail.class,
						"transactionDetail")
				.add(Restrictions.eq("transactionDetail.transaction.id",
						transactionId));
		return criteria.list();
	}

	public InventoryStoreDrugTransactionDetail saveStoreDrugTransactionDetail(
			InventoryStoreDrugTransactionDetail storeTransactionDetail)
			throws DAOException {
		return (InventoryStoreDrugTransactionDetail) sessionFactory
				.getCurrentSession().merge(storeTransactionDetail);
	}

	public int countStoreDrugTransactionDetail(Integer storeId,
			Integer categoryId, String drugName, String formulationName,
			String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugTransactionDetail.class,
						"transactionDetail")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("transactionDetail.transaction", "transaction")
				.createAlias("transactionDetail.drug", "drug")
				.createAlias("transactionDetail.formulation", "formulation");
		criteria.add(Restrictions.eq("transaction.typeTransaction",
				ActionValue.TRANSACTION[0]));
		if (storeId != null) {
			criteria.add(Restrictions.eq("transaction.store.id", storeId));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("drug.category.id", categoryId));
		}
		if (!StringUtils.isBlank(drugName)) {
			criteria.add(Restrictions.like("drug.name", "%" + drugName + "%"));
		}
		if (!StringUtils.isBlank(formulationName)) {
			criteria.add(Restrictions.like("formulation.name", "%"
					+ formulationName + "%"));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType1>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType2>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType3>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreDrugTransactionDetail getStoreDrugTransactionDetailById(
			Integer id) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreDrugTransactionDetail.class, "transactionDetail");
		criteria.add(Restrictions.eq("transactionDetail.id", id));
		return (InventoryStoreDrugTransactionDetail) criteria.uniqueResult();
	}

	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(
			Integer storeId, Integer drugId, Integer formulationId,
			boolean haveQuantity) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.add(Restrictions.eq("transaction.store.id", storeId))
				.add(Restrictions.eq("transactionDetail.drug.id", drugId))
				.add(Restrictions.eq("transactionDetail.formulation.id",
						formulationId))
				.add(Restrictions.eq("transaction.typeTransaction",
						ActionValue.TRANSACTION[0]));

		String date = formatterExt.format(new Date());
		String startFromDate = date + " 00:00:00";

		if (haveQuantity) {
			criteria.add(Restrictions
					.gt("transactionDetail.currentQuantity", 0));
			try {
				criteria.add(Restrictions.ge("transactionDetail.dateExpiry",
						formatter.parse(startFromDate)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		criteria.addOrder(Order.asc("transactionDetail.dateExpiry"));
		List<InventoryStoreDrugTransactionDetail> l = criteria.list();
		return l;
	}

	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(
			Integer storeId, Integer drugId, Integer formulationId,
			Integer isExpiry) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.add(Restrictions.eq("transaction.store.id", storeId))
				.add(Restrictions.eq("transactionDetail.drug.id", drugId))
				.add(Restrictions.eq("transactionDetail.formulation.id",
						formulationId));
		criteria.addOrder(Order.desc("transactionDetail.createdOn"));
		if (isExpiry != null && isExpiry == 1) {
			criteria.add(Restrictions.lt("transactionDetail.dateExpiry",
					new Date()));
		} else {
			criteria.add(Restrictions.ge("transactionDetail.dateExpiry",
					new Date()));
		}
		List<InventoryStoreDrugTransactionDetail> l = criteria.list();
		return l;
	}

	public Integer sumCurrentQuantityDrugOfStore(Integer storeId,
			Integer drugId, Integer formulationId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.add(Restrictions.eq("transaction.store.id", storeId))
				.add(Restrictions.eq("transaction.typeTransaction",
						ActionValue.TRANSACTION[0]))
				.add(Restrictions.eq("transactionDetail.drug.id", drugId))
				.add(Restrictions.eq("transactionDetail.formulation.id",
						formulationId));

		criteria.add(Restrictions.gt("transactionDetail.currentQuantity", 0));
		criteria.add(Restrictions
				.gt("transactionDetail.dateExpiry", new Date()));
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.sqlProjection( "sum(current_quantity) as current_quantity", new String[] {"current_quantity"},new Type[] {StandardBasicTypes.INTEGER}));
		criteria.setProjection(proList);
		Object l = criteria.uniqueResult();
		return l != null ? (Integer) l : 0;
	}

	public List<InventoryStoreDrugTransactionDetail> listStoreDrugAvaiable(
			Integer storeId, Collection<Integer> drugs,
			Collection<Integer> formulations) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.groupProperty("drug"))
				.add(Projections.groupProperty("formulation"))
				.add(Projections.sqlProjection( "sum(current_quantity) as current_quantity", new String[] {"current_quantity"},new Type[] {StandardBasicTypes.INTEGER}));
		criteria.add(Restrictions.eq("transaction.store.id", storeId));
		if (drugs != null) {
			criteria.createCriteria("transactionDetail.drug",
					Criteria.INNER_JOIN).add(Restrictions.in("id", drugs));
		}
		criteria.add(Restrictions.eq("transaction.typeTransaction",
				ActionValue.TRANSACTION[0]));
		if (formulations != null) {
			criteria.createCriteria("transactionDetail.formulation",
					Criteria.INNER_JOIN).add(
					Restrictions.in("id", formulations));
		}
		criteria.setProjection(proList);
		criteria.add(Restrictions
				.ge("transactionDetail.dateExpiry", new Date()));
		List<Object> lst = criteria.list();
		if (lst == null || lst.size() == 0)
			return null;
		List<InventoryStoreDrugTransactionDetail> list = new ArrayList<InventoryStoreDrugTransactionDetail>();
		// System.out.println("lst size: "+lst.size());
		for (int i = 0; i < lst.size(); i++) {
			Object[] row = (Object[]) lst.get(i);
			InventoryStoreDrugTransactionDetail tDetail = new InventoryStoreDrugTransactionDetail();
			tDetail.setDrug((InventoryDrug) row[0]);
			tDetail.setFormulation((InventoryDrugFormulation) row[1]);
			tDetail.setCurrentQuantity((Integer) row[2]);
			list.add(tDetail);
			// System.out.println("I: "+i+" drug: "+tDetail.getDrug().getName()+" formulation: "+tDetail.getFormulation().getName()+" quantity: "+tDetail.getCurrentQuantity());
		}
		return list;
	}

	public List<InventoryStoreDrugTransactionDetail> listViewStockBalance(
			Integer storeId, Integer categoryId, String drugName,String attribute,
			String fromDate, String toDate, boolean isExpiry, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.createAlias("transactionDetail.drug", "drugAlias")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.groupProperty("drug"))
				.add(Projections.groupProperty("formulation"))
				.add(Projections.groupProperty("attribute"))
				.add(Projections.groupProperty("reorderPoint"))
				.add(Projections.sqlProjection( "sum(current_quantity) as current_quantity", new String[] {"current_quantity"},new Type[] {StandardBasicTypes.INTEGER}))
				.add(Projections.sqlProjection( "sum(quantity)  as quantity", new String[] {"quantity"},new Type[] {StandardBasicTypes.INTEGER}))
				.add(Projections.sqlProjection( "sum(issue_quantity)  as issue_quantity", new String[] {"issue_quantity"},new Type[] {StandardBasicTypes.INTEGER} ));
		criteria.add(Restrictions.eq("transaction.store.id", storeId));
		if (categoryId != null) {
			criteria.add(Restrictions.eq("drugAlias.category.id", categoryId));
		}
		if (!StringUtils.isBlank(drugName)) {
			criteria.add(Restrictions.like("drugAlias.name", "%" + drugName
					+ "%"));
		}
		if (!StringUtils.isBlank(attribute)) {
			criteria.add(Restrictions.like("transactionDetail.attribute", "%" + attribute
					+ "%"));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startFromDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventorySubStoreIndent>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		if (isExpiry) {
			criteria.add(Restrictions.lt("transactionDetail.dateExpiry",
					new Date()));
		} else {
			criteria.add(Restrictions.ge("transactionDetail.dateExpiry",
					new Date()));
		}

		/*
		 * Sagar Bele : 13-08-2012 Bug #330 ( [INVENTORY]-error in Current
		 * quantity of pharmacy )
		 */
		criteria.add(Restrictions.ge("transactionDetail.currentQuantity", 0));

		criteria.setProjection(proList);
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<Object> lst = criteria.list();
		if (lst == null || lst.size() == 0)
			return null;
		List<InventoryStoreDrugTransactionDetail> list = new ArrayList<InventoryStoreDrugTransactionDetail>();
		for (int i = 0; i < lst.size(); i++) {
			Object[] row = (Object[]) lst.get(i);
			InventoryStoreDrugTransactionDetail tDetail = new InventoryStoreDrugTransactionDetail();
			tDetail.setDrug((InventoryDrug) row[0]);
			tDetail.setFormulation((InventoryDrugFormulation) row[1]);
			tDetail.setAttribute((String) row[2]);
			tDetail.setCurrentQuantity((Integer) row[4]);
			tDetail.setQuantity((Integer) row[5]);
			tDetail.setIssueQuantity((Integer) row[6]);
			tDetail.setReorderPoint((Integer) row[3]);
			list.add(tDetail);
		}

		return list;
	}

	public Integer countViewStockBalance(Integer storeId, Integer categoryId,
			String drugName,String attribute, String fromDate, String toDate, boolean isExpiry)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.createAlias("transactionDetail.drug", "drugAlias");

		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.groupProperty("drug"))
				.add(Projections.groupProperty("formulation"))
				.add(Projections.groupProperty("attribute"))
				.add(Projections.groupProperty("reorderPoint"))
				.add(Projections.sum("currentQuantity"))
				.add(Projections.sum("quantity"))
				.add(Projections.sum("issueQuantity"));
		criteria.add(Restrictions.eq("transaction.store.id", storeId));
		if (categoryId != null) {
			criteria.add(Restrictions.eq("drugAlias.category.id", categoryId));
		}
		if (!StringUtils.isBlank(drugName)) {
			criteria.add(Restrictions.like("drugAlias.name", "%" + drugName
					+ "%"));
		}
		if (!StringUtils.isBlank(attribute)) {
			criteria.add(Restrictions.like("transactionDetail.attribute", "%" + attribute
					+ "%"));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startFromDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventorySubStoreIndent>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		if (isExpiry) {
			criteria.add(Restrictions.lt("transactionDetail.dateExpiry",
					new Date()));
		} else {
			criteria.add(Restrictions.ge("transactionDetail.dateExpiry",
					new Date()));
		}
		criteria.setProjection(proList);
		List<Object> list = criteria.list();
		Number total = 0;
		if (!CollectionUtils.isEmpty(list)) {
			total = (Number) list.size();
		}
		return total.intValue();
	}

	public int checkExistDrugTransactionDetail(Integer drugId)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugTransactionDetail.class,
						"transactionDetail")
				.add(Restrictions.eq("transactionDetail.drug.id", drugId));
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	/**
	 * InventoryStoreDrugIndent
	 */

	public List<InventoryStoreDrugIndent> listSubStoreIndent(Integer storeId,
			String name, Integer status, String fromDate, String toDate,
			int min, int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrugIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("indent.store", "store");
		criteria.add(Restrictions.eq("store.id", storeId));

		if (status != null) {
			criteria.add(Restrictions.eq("indent.subStoreStatus", status));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventorySubStoreIndent>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		criteria.addOrder(Order.desc("indent.createdOn"));
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryStoreDrugIndent> l = criteria.list();
		return l;
	}

	public int countSubStoreIndent(Integer storeId, String name,
			Integer status, String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrugIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.rowCount())
				.createAlias("indent.store", "store");
		criteria.add(Restrictions.eq("store.id", storeId));

		if (status != null) {
			criteria.add(Restrictions.eq("indent.subStoreStatus", status));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventorySubStoreIndent>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public List<InventoryStoreDrugIndent> listMainStoreIndent(Integer id,
			Integer mainStoreId, Integer subStoreId, String name,
			Integer status, String fromDate, String toDate, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrugIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("indent.store", "store");

		criteria.add(Restrictions.eq("indent.mainStore.id", mainStoreId));

		if (subStoreId != null) {

			criteria.add(Restrictions.eq("store.id", subStoreId));
		}

		if (id != null && id > 0) {

			criteria.add(Restrictions.eq("indent.id", id));
		}

		criteria.add(Restrictions.ge("indent.subStoreStatus",
				ActionValue.INDENT_SUBSTORE[1]));
		if (status != null) {
			criteria.add(Restrictions.eq("indent.mainStoreStatus", status));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		criteria.addOrder(Order.desc("indent.createdOn"));
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryStoreDrugIndent> l = criteria.list();
		return l;
	}

	public List<InventoryStoreDrugIndent> listStoreDrugIndent(Integer StoreId,
			String name, String fromDate, String toDate, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrugIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("indent.store", "store");

		criteria.add(Restrictions.eq("store.id", StoreId));

		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		criteria.addOrder(Order.desc("indent.createdOn"));
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryStoreDrugIndent> l = criteria.list();
		return l;
	}

	public int countStoreDrugIndent(Integer StoreId, String name,
			String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrugIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("indent.store", "store");

		criteria.add(Restrictions.eq("store.id", StoreId));

		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public int countMainStoreIndent(Integer id, Integer mainStoreId,
			Integer subStoreId, String name, Integer status, String fromDate,
			String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrugIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("indent.store", "store");

		criteria.add(Restrictions.eq("indent.mainStore.id", mainStoreId));
		if (id != null && id > 0) {
			criteria.add(Restrictions.eq("indent.id", id));
		}
		if (subStoreId != null) {

			criteria.add(Restrictions.eq("store.id", subStoreId));
		}
		criteria.add(Restrictions.ge("indent.subStoreStatus",
				ActionValue.INDENT_SUBSTORE[1]));
		if (status != null) {
			criteria.add(Restrictions.eq("indent.mainStoreStatus", status));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		// System.out.println("count total transfer: "+rs);
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreDrugIndent saveStoreDrugIndent(
			InventoryStoreDrugIndent storeDrugIndent) throws DAOException {
		return (InventoryStoreDrugIndent) sessionFactory.getCurrentSession()
				.merge(storeDrugIndent);
	}

	public void deleteStoreDrugIndent(InventoryStoreDrugIndent storeDrugIndent)
			throws DAOException {
		sessionFactory.getCurrentSession().delete(storeDrugIndent);
	}

	public InventoryStoreDrugIndent getStoreDrugIndentById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrugIndent.class, "indent")
				.add(Restrictions.eq("indent.id", id));
		return (InventoryStoreDrugIndent) criteria.uniqueResult();
	}

	/**
	 * InventoryStoreDrugIndentDetail
	 */
	public List<InventoryStoreDrugIndentDetail> listStoreDrugIndentDetail(
			Integer indentId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugIndentDetail.class,
						"indentDetail")
				.add(Restrictions.eq("indentDetail.indent.id", indentId));
		List<InventoryStoreDrugIndentDetail> l = criteria.list();
		return l;
	}

	public int checkExistDrugIndentDetail(Integer drugId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugIndentDetail.class,
						"indentDetail")
				.add(Restrictions.eq("indentDetail.drug.id", drugId));
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public List<InventoryStoreDrugIndentDetail> listStoreDrugIndentDetail(
			Integer storeId, Integer categoryId, String indentName,
			String drugName, String fromDate, String toDate, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugIndentDetail.class,
						"indentDetail")
				.createAlias("indentDetail.indent", "indent")
				.createAlias("indentDetail.drug", "drug")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("indent.store.id", storeId));
		if (categoryId != null) {
			criteria.add(Restrictions.eq("drug.category.id", categoryId));
		}
		if (!StringUtils.isBlank(indentName)) {
			criteria.add(Restrictions.like("indent.name", "%" + indentName
					+ "%"));
		}
		if (!StringUtils.isBlank(drugName)) {
			criteria.add(Restrictions.like("drug.name", drugName));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listStoreDrugIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listStoreDrugIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listStoreDrugIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryStoreDrugIndentDetail> l = criteria.list();
		return l;
	}

	@Override
	public int countStoreDrugIndentDetail(Integer storeId, Integer categoryId,
			String indentName, String drugName, String fromDate, String toDate)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugIndentDetail.class,
						"indentDetail")
				.createAlias("indentDetail.indent", "indent")
				.createAlias("indentDetail.drug", "drug")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("indent.store.id", storeId));
		if (categoryId != null) {
			criteria.add(Restrictions.eq("drug.category.id", categoryId));
		}
		if (!StringUtils.isBlank(indentName)) {
			criteria.add(Restrictions.like("indent.name", "%" + indentName
					+ "%"));
		}
		if (!StringUtils.isBlank(drugName)) {
			criteria.add(Restrictions.like("drug.name", drugName));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("countStoreDrugIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("countStoreDrugIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("countStoreDrugIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreDrugIndentDetail saveStoreDrugIndentDetail(
			InventoryStoreDrugIndentDetail storeDrugIndentDetail)
			throws DAOException {
		return (InventoryStoreDrugIndentDetail) sessionFactory
				.getCurrentSession().merge(storeDrugIndentDetail);
	}

	public InventoryStoreDrugIndentDetail getStoreDrugIndentDetailById(
			Integer id) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreDrugIndentDetail.class, "indentDetail");
		criteria.add(Restrictions.eq("indentDetail.indent.id", id));

		return (InventoryStoreDrugIndentDetail) criteria.uniqueResult();
	}

	/**
	 * InventoryStoreDrugPatient
	 */
	public List<InventoryStoreDrugPatient> listStoreDrugPatient(
			Integer storeId, Integer receiptId, String name, String fromDate, String toDate,
			int min, int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrugPatient.class, "bill")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("bill.store", "store");

		if (storeId != null) {

			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.or(
					Restrictions.like("bill.identifier", "%" + name + "%"),
					Restrictions.like("bill.name", "%" + name + "%")));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		if (receiptId != null) {

			criteria.add(Restrictions.eq("bill.id", receiptId));
		}
		criteria.addOrder(Order.desc("bill.createdOn"));
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryStoreDrugPatient> l = criteria.list();
		return l;
	}

	public int countStoreDrugPatient(Integer storeId, String name,
			String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrugPatient.class, "bill")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("bill.store", "store")
				.setProjection(Projections.rowCount());

		if (storeId != null) {
			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.or(
					Restrictions.like("bill.identifier", "%" + name + "%"),
					Restrictions.like("bill.name", "%" + name + "%")));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreDrugPatient saveStoreDrugPatient(
			InventoryStoreDrugPatient bill) throws DAOException {
		return (InventoryStoreDrugPatient) sessionFactory.getCurrentSession()
				.merge(bill);
	}

	public InventoryStoreDrugPatient getStoreDrugPatientById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreDrugPatient.class, "drugPatient");
		criteria.add(Restrictions.eq("patientBill.id", id));
		return (InventoryStoreDrugPatient) criteria.uniqueResult();
	}

	/**
	 * InventoryStoreDrugPatientDetail
	 */
	public List<InventoryStoreDrugPatientDetail> listStoreDrugPatientDetail(
			Integer storeDrugPatientId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugPatientDetail.class,
						"billDetail")
				.add(Restrictions.eq("billDetail.storeDrugPatient.id",
						storeDrugPatientId));
		List<InventoryStoreDrugPatientDetail> l = criteria.list();
		return l;
	}

	public InventoryStoreDrugPatientDetail saveStoreDrugPatientDetail(
			InventoryStoreDrugPatientDetail storeDrugPatientDetail)
			throws DAOException {
		return (InventoryStoreDrugPatientDetail) sessionFactory
				.getCurrentSession().merge(storeDrugPatientDetail);
	}

	public InventoryStoreDrugPatientDetail getStoreDrugPatientDetailById(
			Integer id) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugPatientDetail.class,
						"billDetail").add(Restrictions.eq("billDetail.id", id));

		return (InventoryStoreDrugPatientDetail) criteria.uniqueResult();
	}

	// change here
	/**
	 * StoreDrug
	 */

	public List<InventoryStoreItem> listStoreItem(Integer storeId,
			Integer categoryId, String itemName, Integer reOrderQty, int min,
			int max) throws DAOException {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItem.class, "storeItem")
				.createAlias("storeItem.item", "item")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (storeId != null) {
			criteria.add(Restrictions.eq("storeItem.store.id", storeId));
		}
		if (!StringUtils.isBlank(itemName)) {
			criteria.add(Restrictions.like("item.name", "%" + itemName + "%"));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("item.category.id", categoryId));
		}
		if (reOrderQty != null) {
			criteria.add(Restrictions.eq("storeItem.reorderQty", reOrderQty));
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryStoreItem> l = criteria.list();

		return l;

	}

	public int countStoreItem(Integer storeId, Integer categoryId,
			String itemName, Integer reOrderQty) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItem.class, "storeItem")
				.createAlias("storeItem.item", "item")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (storeId != null) {
			criteria.add(Restrictions.eq("storeItem.store.id", storeId));
		}
		if (!StringUtils.isBlank(itemName)) {
			criteria.add(Restrictions.like("item.name", "%" + itemName + "%"));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("item.category.id", categoryId));
		}
		if (reOrderQty != null) {
			criteria.add(Restrictions.eq("storeItem.reorderQty", reOrderQty));
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreItem getStoreItemById(Integer id) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreItem.class, "storeItem");
		criteria.add(Restrictions.eq("storeItem.id", id));
		return (InventoryStoreItem) criteria.uniqueResult();
	}

	public InventoryStoreItem getStoreItem(Integer storeId, Integer itemId,
			Integer specificationId) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItem.class, "storeItem")
				.add(Restrictions.eq("storeItem.store.id", storeId))
				.add(Restrictions.eq("storeItem.item.id", itemId));
		if (specificationId != null) {
			criteria.add(Restrictions.eq("storeItem.specification.id",
					specificationId));
		} else {
			criteria.add(Restrictions.isNull("storeItem.specification"));
		}
		return (InventoryStoreItem) criteria.uniqueResult();
	}

	public InventoryStoreItem saveStoreItem(InventoryStoreItem storeItem)
			throws DAOException {
		return (InventoryStoreItem) sessionFactory.getCurrentSession().merge(
				storeItem);
	}

	/**
	 * StoreItemTransaction
	 */

	public List<InventoryStoreItemTransaction> listStoreItemTransaction(
			Integer transactionType, Integer storeId, String description,
			String fromDate, String toDate, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreItemTransaction.class);
		if (storeId != null) {
			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(description)) {
			criteria.add(Restrictions.like("description", "%" + description
					+ "%"));
		}
		if (transactionType != null) {
			criteria.add(Restrictions.eq("typeTransaction", transactionType));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		criteria.addOrder(Order.desc("createdOn"));
		List<InventoryStoreItemTransaction> l = criteria.list();

		return l;
	}

	public List<InventoryStoreItemTransaction> listStoreItemTransaction(
			Integer transactionType, Integer storeId, String description,
			String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreItemTransaction.class);
		if (storeId != null) {
			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(description)) {
			criteria.add(Restrictions.like("description", "%" + description
					+ "%"));
		}
		if (transactionType != null) {
			criteria.add(Restrictions.eq("typeTransaction", transactionType));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		}
		List<InventoryStoreItemTransaction> l = criteria.list();

		return l;
	}

	public InventoryStoreItemTransaction saveStoreItemTransaction(
			InventoryStoreItemTransaction storeTransaction) throws DAOException {
		return (InventoryStoreItemTransaction) sessionFactory
				.getCurrentSession().merge(storeTransaction);
	}

	public int countStoreItemTransaction(Integer transactionType,
			Integer storeId, String description, String fromDate, String toDate)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreItemTransaction.class);
		if (storeId != null) {
			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(description)) {
			criteria.add(Restrictions.like("description", "%" + description
					+ "%"));
		}
		if (transactionType != null) {
			criteria.add(Restrictions.eq("typeTransaction", transactionType));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge("createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"createdOn", formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error convert date: " + e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreItemTransaction getStoreItemTransactionById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreItemTransaction.class, "StoreItemTransaction");
		criteria.add(Restrictions.eq("StoreItemTransaction.id", id));
		return (InventoryStoreItemTransaction) criteria.uniqueResult();
	}

	public InventoryStoreItemTransaction getStoreItemTransactionByParentId(
			Integer parentId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemTransaction.class,
						"StoreItemTransaction")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("StoreItemTransaction.parent", "parent");
		criteria.add(Restrictions.eq("parent.id", parentId));
		return (InventoryStoreItemTransaction) criteria.uniqueResult();
	}

	/**
	 * StoreItemTransactionDetail
	 */
	public int checkExistItemTransactionDetail(Integer itemId)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemTransactionDetail.class,
						"transactionDetail")
				.add(Restrictions.eq("transactionDetail.item.id", itemId));
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(
			Integer storeId, Integer categoryId, String itemName,
			String specificationName, String fromDate, String toDate, int min,
			int max) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemTransactionDetail.class,
						"transactionDetail")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("transactionDetail.transaction", "transaction")
				.createAlias("transactionDetail.item", "item");
		criteria.add(Restrictions.eq("transaction.typeTransaction",
				ActionValue.TRANSACTION[0]));
		if (storeId != null) {
			criteria.add(Restrictions.eq("transaction.store.id", storeId));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("item.category.id", categoryId));
		}
		if (!StringUtils.isBlank(itemName)) {
			criteria.add(Restrictions.like("item.name", "%" + itemName + "%"));
		}
		if (!StringUtils.isBlank(specificationName)) {
			criteria.createAlias("transactionDetail.specification",
					"specification");
			criteria.add(Restrictions.like("specification.name", "%"
					+ specificationName + "%"));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType1>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType2>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType3>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryStoreItemTransactionDetail> l = criteria.list();

		return l;
	}

	@Override
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(
			Integer transactionId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemTransactionDetail.class,
						"transactionDetail")
				.add(Restrictions.eq("transactionDetail.transaction.id",
						transactionId));
		return criteria.list();
	}

	public InventoryStoreItemTransactionDetail saveStoreItemTransactionDetail(
			InventoryStoreItemTransactionDetail storeTransactionDetail)
			throws DAOException {
		return (InventoryStoreItemTransactionDetail) sessionFactory
				.getCurrentSession().merge(storeTransactionDetail);
	}

	public int countStoreItemTransactionDetail(Integer storeId,
			Integer categoryId, String itemName, String specificationName,
			String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemTransactionDetail.class,
						"transactionDetail")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("transactionDetail.transaction", "transaction")
				.createAlias("transactionDetail.item", "item");
		criteria.add(Restrictions.eq("transaction.typeTransaction",
				ActionValue.TRANSACTION[0]));
		if (storeId != null) {
			criteria.add(Restrictions.eq("transaction.store.id", storeId));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("item.category.id", categoryId));
		}
		if (!StringUtils.isBlank(itemName)) {
			criteria.add(Restrictions.like("item.name", "%" + itemName + "%"));
		}
		if (!StringUtils.isBlank(specificationName)) {
			criteria.createAlias("transactionDetail.specification",
					"specification");
			criteria.add(Restrictions.like("specification.name", "%"
					+ specificationName + "%"));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType1>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType2>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("transaction.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("transaction.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventoryStoreTransactionItemType3>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreItemTransactionDetail getStoreItemTransactionDetailById(
			Integer id) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreItemTransactionDetail.class, "transactionDetail");
		criteria.add(Restrictions.eq("transactionDetail.id", id));
		return (InventoryStoreItemTransactionDetail) criteria.uniqueResult();
	}

	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(
			Integer storeId, Integer itemId, Integer specificationId,
			boolean haveQuantity) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.add(Restrictions.eq("transaction.store.id", storeId))
				.add(Restrictions.eq("transactionDetail.item.id", itemId))
				.add(Restrictions.eq("transaction.typeTransaction",
						ActionValue.TRANSACTION[0]));
		if (specificationId != null && specificationId > 0) {
			criteria.add(Restrictions.eq("transactionDetail.specification.id",
					specificationId));
		} else {
			criteria.add(Restrictions.isNull("transactionDetail.specification"));
		}
		if (haveQuantity) {
			criteria.add(Restrictions
					.gt("transactionDetail.currentQuantity", 0));
		}

		criteria.addOrder(Order.asc("transactionDetail.currentQuantity"));
		List<InventoryStoreItemTransactionDetail> l = criteria.list();
		return l;
	}

	@Override
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(
			Integer storeId, Integer itemId, Integer specificationId, int min,
			int max) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.add(Restrictions.eq("transactionDetail.item.id", itemId));

		if (storeId != null && storeId > 0) {
			criteria.add(Restrictions.eq("transaction.store.id", storeId));
		}
		if (specificationId != null && specificationId > 0) {
			criteria.add(Restrictions.eq("transactionDetail.specification.id",
					specificationId));
		} else {
			criteria.add(Restrictions.isNull("transactionDetail.specification"));
		}
		criteria.addOrder(Order.desc("transactionDetail.createdOn"));
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryStoreItemTransactionDetail> l = criteria.list();
		return l;
	}

	@Override
	public Integer sumStoreItemCurrentQuantity(Integer storeId, Integer itemId,
			Integer specificationId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.add(Restrictions.eq("transaction.store.id", storeId))
				.add(Restrictions.eq("transaction.typeTransaction",
						ActionValue.TRANSACTION[0]))
				.add(Restrictions.eq("transactionDetail.item.id", itemId));
		if (specificationId != null && specificationId > 0) {
			criteria.add(Restrictions.eq("transactionDetail.specification.id",
					specificationId));
		} else {
			criteria.add(Restrictions.isNull("transactionDetail.specification"));
		}

		criteria.add(Restrictions.gt("transactionDetail.currentQuantity", 0));
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.sqlProjection( "sum(current_quantity) as current_quantity", new String[] {"current_quantity"},new Type[] {StandardBasicTypes.INTEGER}));
		criteria.setProjection(proList);
		Object l = criteria.uniqueResult();
		return l != null ? (Integer) l : 0;
	}

	@Override
	public List<InventoryStoreItemTransactionDetail> listStoreItemAvaiable(
			Integer storeId, Collection<Integer> items,
			Collection<Integer> specifications) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.groupProperty("item"))
				.add(Projections.groupProperty("specification"))
				.add(Projections.sqlProjection( "sum(current_quantity) as current_quantity", new String[] {"current_quantity"},new Type[] {StandardBasicTypes.INTEGER}));
		criteria.add(Restrictions.eq("transaction.store.id", storeId));
		if (CollectionUtils.isNotEmpty(items)) {
			criteria.createCriteria("transactionDetail.item",
					Criteria.INNER_JOIN).add(Restrictions.in("id", items));
		}
		criteria.add(Restrictions.eq("transaction.typeTransaction",
				ActionValue.TRANSACTION[0]));
		if (CollectionUtils.isNotEmpty(specifications)) {
			criteria.createCriteria("transactionDetail.specification",
					Criteria.LEFT_JOIN).add(
					Restrictions.in("id", specifications));
		}
		criteria.setProjection(proList);
		List<Object> lst = criteria.list();
		if (lst == null || lst.size() == 0)
			return null;
		List<InventoryStoreItemTransactionDetail> list = new ArrayList<InventoryStoreItemTransactionDetail>();
		// System.out.println("lst size: "+lst.size());
		for (int i = 0; i < lst.size(); i++) {
			Object[] row = (Object[]) lst.get(i);
			InventoryStoreItemTransactionDetail tDetail = new InventoryStoreItemTransactionDetail();
			tDetail.setItem((InventoryItem) row[0]);
			tDetail.setSpecification((InventoryItemSpecification) row[1]);
			tDetail.setCurrentQuantity((Integer) row[2]);
			list.add(tDetail);
			// System.out.println("I: "+i+" item: "+tDetail.getItem().getName()+" specification: "+(tDetail.getSpecification()
			// != null ?tDetail.getSpecification().getName() : " null ")
			// +" quantity: "+tDetail.getCurrentQuantity());
		}
		// System.out.println("list available: "+list);
		return list;
	}
	//edited
	@Override
	public List<InventoryStoreItemTransactionDetail> listStoreItemViewStockBalance(
			Integer storeId, Integer categoryId, String itemName,String attribute,
			String fromDate, String toDate, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.createAlias("transactionDetail.item", "itemAlias")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.groupProperty("item"))
				.add(Projections.groupProperty("specification"))
				//edited
				.add(Projections.groupProperty("attribute"))
				.add(Projections.sqlProjection( "sum(current_quantity) as current_quantity", new String[] {"current_quantity"},new Type[] {StandardBasicTypes.INTEGER}))
				.add(Projections.sqlProjection( "sum(quantity)  as quantity", new String[] {"quantity"},new Type[] {StandardBasicTypes.INTEGER}))
				.add(Projections.sqlProjection( "sum(issue_quantity)  as issue_quantity", new String[] {"issue_quantity"},new Type[] {StandardBasicTypes.INTEGER} ));

		criteria.add(Restrictions.eq("transaction.store.id", storeId));
		if (categoryId != null) {
			criteria.add(Restrictions
					.eq("itemAlias.subCategory.id", categoryId));
		}
		if (!StringUtils.isBlank(itemName)) {
			criteria.add(Restrictions.like("itemAlias.name", "%" + itemName
					+ "%"));
		}
		//new
		if (!StringUtils.isBlank(attribute)) {
			criteria.add(Restrictions.like("transactionDetail.attribute", "%" + attribute
					+ "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startFromDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventorySubStoreIndent>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}

		criteria.setProjection(proList);
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<Object> lst = criteria.list();
		if (lst == null || lst.size() == 0)
			return null;
		List<InventoryStoreItemTransactionDetail> list = new ArrayList<InventoryStoreItemTransactionDetail>();
		for (int i = 0; i < lst.size(); i++) {
			Object[] row = (Object[]) lst.get(i);
			InventoryStoreItemTransactionDetail tDetail = new InventoryStoreItemTransactionDetail();
			tDetail.setItem((InventoryItem) row[0]);
			tDetail.setSpecification((InventoryItemSpecification) row[1]);
			tDetail.setAttribute((String) row[2]);
			tDetail.setCurrentQuantity((Integer) row[3]);
			tDetail.setQuantity((Integer) row[4]);
			tDetail.setIssueQuantity((Integer) row[5]);
			list.add(tDetail);
		}

		return list;
	}
	//edited
	@Override
	public Integer countStoreItemViewStockBalance(Integer storeId,
			Integer categoryId, String itemName,String attribute, String fromDate, String toDate)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemTransactionDetail.class,
						"transactionDetail")
				.createAlias("transactionDetail.transaction", "transaction")
				.createAlias("transactionDetail.item", "itemAlias");

		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.groupProperty("item"))
				.add(Projections.groupProperty("specification"))
				//new
				.add(Projections.groupProperty("attribute"))
				.add(Projections.sqlProjection( "sum(current_quantity) as current_quantity", new String[] {"current_quantity"},new Type[] {StandardBasicTypes.INTEGER}))
				.add(Projections.sqlProjection( "sum(quantity)  as quantity", new String[] {"quantity"},new Type[] {StandardBasicTypes.INTEGER}))
				.add(Projections.sqlProjection( "sum(issue_quantity)  as issue_quantity", new String[] {"issue_quantity"},new Type[] {StandardBasicTypes.INTEGER} ));
		criteria.add(Restrictions.eq("transaction.store.id", storeId));
		if (categoryId != null) {
			criteria.add(Restrictions
					.eq("itemAlias.subCategory.id", categoryId));
		}
		if (!StringUtils.isBlank(itemName)) {
			criteria.add(Restrictions.like("itemAlias.name", "%" + itemName
					+ "%"));
		}
		//new
		if (!StringUtils.isBlank(attribute)) {
			criteria.add(Restrictions.like("transactionDetail.attribute", "%" + attribute
					+ "%"));
		}


		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startFromDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(Restrictions.ge(
						"transactionDetail.createdOn",
						formatter.parse(startToDate)), Restrictions.le(
						"transactionDetail.createdOn",
						formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventorySubStoreIndent>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		criteria.setProjection(proList);
		List<Object> list = criteria.list();
		Number total = 0;
		if (!CollectionUtils.isEmpty(list)) {
			total = (Number) list.size();
		}
		return total.intValue();
	}

	/**
	 * InventoryStoreItemIndent
	 */
	@Override
	public List<InventoryStoreItemIndent> listSubStoreItemIndent(
			Integer storeId, String name, Integer status, String fromDate,
			String toDate, int min, int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItemIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("indent.store", "store");
		criteria.add(Restrictions.eq("store.id", storeId));

		if (status != null) {
			criteria.add(Restrictions.eq("indent.subStoreStatus", status));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listInventorySubStoreIndent>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		criteria.addOrder(Order.desc("indent.createdOn"));
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryStoreItemIndent> l = criteria.list();
		return l;
	}

	@Override
	public int countSubStoreItemIndent(Integer storeId, String name,
			Integer status, String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItemIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("indent.store", "store");
		criteria.add(Restrictions.eq("store.id", storeId));

		if (status != null) {
			criteria.add(Restrictions.eq("indent.subStoreStatus", status));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countSubStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	@Override
	public List<InventoryStoreItemIndent> listMainStoreItemIndent(Integer id,
			Integer mainStoreId, Integer subStoreId, String name,
			Integer status, String fromDate, String toDate, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItemIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("indent.store", "store");

		criteria.add(Restrictions.eq("indent.mainStore.id", mainStoreId));

		if (id != null && id > 0) {

			criteria.add(Restrictions.eq("indent.id", id));
		}

		if (subStoreId != null) {

			criteria.add(Restrictions.eq("indent.store.id", subStoreId));
		}
		criteria.add(Restrictions.ge("indent.subStoreStatus",
				ActionValue.INDENT_SUBSTORE[1]));
		if (status != null) {
			criteria.add(Restrictions.eq("indent.mainStoreStatus", status));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		criteria.addOrder(Order.desc("indent.createdOn"));
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryStoreItemIndent> l = criteria.list();
		return l;
	}

	public List<InventoryStoreItemIndent> listStoreItemIndent(Integer StoreId,
			String name, String fromDate, String toDate, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItemIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("indent.store", "store");

		criteria.add(Restrictions.eq("store.id", StoreId));

		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		criteria.addOrder(Order.desc("indent.createdOn"));
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryStoreItemIndent> l = criteria.list();
		return l;
	}

	public int countStoreItemIndent(Integer StoreId, String name,
			String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItemIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("indent.store", "store");

		criteria.add(Restrictions.eq("store.id", StoreId));

		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	@Override
	public int countMainStoreItemIndent(Integer id, Integer mainStoreId,
			Integer subStoreId, String name, Integer status, String fromDate,
			String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItemIndent.class, "indent")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("indent.store", "store");

		criteria.add(Restrictions.eq("indent.mainStore.id", mainStoreId));

		if (subStoreId != null) {

			criteria.add(Restrictions.eq("store.id", subStoreId));
		}

		if (id != null && id > 0) {

			criteria.add(Restrictions.eq("indent.id", id));
		}
		criteria.add(Restrictions.ge("indent.subStoreStatus",
				ActionValue.INDENT_SUBSTORE[1]));
		if (status != null) {
			criteria.add(Restrictions.eq("indent.mainStoreStatus", status));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("indent.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listMainStoreIndent>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreItemIndent saveStoreItemIndent(
			InventoryStoreItemIndent StoreItemIndent) throws DAOException {
		return (InventoryStoreItemIndent) sessionFactory.getCurrentSession()
				.merge(StoreItemIndent);
	}

	public void deleteStoreItemIndent(InventoryStoreItemIndent StoreItemIndent)
			throws DAOException {
		sessionFactory.getCurrentSession().delete(StoreItemIndent);
	}

	public InventoryStoreItemIndent getStoreItemIndentById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItemIndent.class, "indent")
				.add(Restrictions.eq("indent.id", id));
		return (InventoryStoreItemIndent) criteria.uniqueResult();
	}

	/**
	 * InventoryStoreItemIndentDetail
	 */
	public List<InventoryStoreItemIndentDetail> listStoreItemIndentDetail(
			Integer indentId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemIndentDetail.class,
						"indentDetail")
				.add(Restrictions.eq("indentDetail.indent.id", indentId));
		List<InventoryStoreItemIndentDetail> l = criteria.list();
		return l;
	}

	public int checkExistItemIndentDetail(Integer itemId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemIndentDetail.class,
						"indentDetail")
				.add(Restrictions.eq("indentDetail.item.id", itemId));
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public List<InventoryStoreItemIndentDetail> listStoreItemIndentDetail(
			Integer storeId, Integer categoryId, String indentName,
			String itemName, String fromDate, String toDate, int min, int max)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemIndentDetail.class,
						"indentDetail")
				.createAlias("indentDetail.indent", "indent")
				.createAlias("indentDetail.item", "item")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (storeId != null) {
			criteria.add(Restrictions.eq("indent.store.id", storeId));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("item.category.id", categoryId));
		}
		if (!StringUtils.isBlank(indentName)) {
			criteria.add(Restrictions.like("indent.name", "%" + indentName
					+ "%"));
		}
		if (!StringUtils.isBlank(itemName)) {
			criteria.add(Restrictions.like("item.name", itemName));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listStoreItemIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listStoreItemIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("listStoreItemIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryStoreItemIndentDetail> l = criteria.list();
		return l;
	}

	@Override
	public int countStoreItemIndentDetail(Integer storeId, Integer categoryId,
			String indentName, String itemName, String fromDate, String toDate)
			throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemIndentDetail.class,
						"indentDetail")
				.createAlias("indentDetail.indent", "indent")
				.createAlias("indentDetail.item", "item")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("indent.store.id", storeId));
		if (categoryId != null) {
			criteria.add(Restrictions.eq("item.category.id", categoryId));
		}
		if (!StringUtils.isBlank(indentName)) {
			criteria.add(Restrictions.like("indent.name", "%" + indentName
					+ "%"));
		}
		if (!StringUtils.isBlank(itemName)) {
			criteria.add(Restrictions.like("item.name", itemName));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("countStoreItemIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("countStoreItemIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("indent.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("indent.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("countStoreItemIndentDetail>>Error convert date: "
								+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreItemIndentDetail saveStoreItemIndentDetail(
			InventoryStoreItemIndentDetail StoreItemIndentDetail)
			throws DAOException {
		return (InventoryStoreItemIndentDetail) sessionFactory
				.getCurrentSession().merge(StoreItemIndentDetail);
	}

	public InventoryStoreItemIndentDetail getStoreItemIndentDetailById(
			Integer id) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreItemIndentDetail.class, "indentDetail");
		criteria.add(Restrictions.eq("indentDetail.indent.id", id));

		return (InventoryStoreItemIndentDetail) criteria.uniqueResult();
	}

	/**
	 * InventoryStoreItemAccount
	 */
	@Override
	public List<InventoryStoreItemAccount> listStoreItemAccount(
			Integer storeId, String name, String fromDate, String toDate,
			int min, int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItemAccount.class, "bill")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("bill.store", "store");

		if (storeId != null) {

			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("bill.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		criteria.addOrder(Order.desc("bill.createdOn"));
		if (max > 0) {
			criteria.setFirstResult(min).setMaxResults(max);
		}
		List<InventoryStoreItemAccount> l = criteria.list();
		return l;
	}

	public int countStoreItemAccount(Integer storeId, String name,
			String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItemAccount.class, "bill")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("bill.store", "store")
				.setProjection(Projections.rowCount());

		if (storeId != null) {
			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("bill.name", "%" + name + "%"));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreItemAccount saveStoreItemAccount(
			InventoryStoreItemAccount bill) throws DAOException {
		return (InventoryStoreItemAccount) sessionFactory.getCurrentSession()
				.merge(bill);
	}

	public InventoryStoreItemAccount getStoreItemAccountById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreItemAccount.class, "itemPatient");
		criteria.add(Restrictions.eq("patientBill.id", id));
		return (InventoryStoreItemAccount) criteria.uniqueResult();
	}

	/**
	 * InventoryStoreItemPatientDetail
	 */
	public List<InventoryStoreItemAccountDetail> listStoreItemAccountDetail(
			Integer itemAccountId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemAccountDetail.class,
						"billDetail")
				.add(Restrictions
						.eq("billDetail.itemAccount.id", itemAccountId));
		List<InventoryStoreItemAccountDetail> l = criteria.list();
		return l;
	}

	public InventoryStoreItemAccountDetail saveStoreItemAccountDetail(
			InventoryStoreItemAccountDetail StoreItemAccountDetail)
			throws DAOException {
		return (InventoryStoreItemAccountDetail) sessionFactory
				.getCurrentSession().merge(StoreItemAccountDetail);
	}

	public InventoryStoreItemAccountDetail getStoreItemAccountDetailById(
			Integer id) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemAccountDetail.class,
						"billDetail").add(Restrictions.eq("billDetail.id", id));

		return (InventoryStoreItemAccountDetail) criteria.uniqueResult();
	}

	public void deleteDrugUnit(InventoryDrugUnit drugUnit) throws DAOException {
		sessionFactory.getCurrentSession().delete(drugUnit);
	}

	// ============================================================================

	/**
	 * InventoryStoreDrugAccount
	 */
	@Override
	public List<InventoryStoreDrugAccount> listStoreDrugAccount(
			Integer storeId, String name, String fromDate, String toDate,
			int min, int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrugAccount.class, "bill")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("bill.store", "store");

		if (storeId != null) {

			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("bill.name", "%" + name + "%"));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		criteria.addOrder(Order.desc("bill.createdOn"));
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryStoreDrugAccount> l = criteria.list();
		return l;
	}

	public int countStoreDrugAccount(Integer storeId, String name,
			String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreDrugAccount.class, "bill")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("bill.store", "store")
				.setProjection(Projections.rowCount());

		if (storeId != null) {
			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.like("bill.name", "%" + name + "%"));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreDrugAccount saveStoreDrugAccount(
			InventoryStoreDrugAccount bill) throws DAOException {
		return (InventoryStoreDrugAccount) sessionFactory.getCurrentSession()
				.merge(bill);
	}

	public InventoryStoreDrugAccount getStoreDrugAccountById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreDrugAccount.class, "drugAccount");
		criteria.add(Restrictions.eq("drugAccount.id", id));
		return (InventoryStoreDrugAccount) criteria.uniqueResult();
	}

	/**
	 * InventoryStoreDrugPatientDetail
	 */
	public List<InventoryStoreDrugAccountDetail> listStoreDrugAccountDetail(
			Integer drugAccountId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugAccountDetail.class,
						"billDetail")
				.add(Restrictions
						.eq("billDetail.drugAccount.id", drugAccountId));
		List<InventoryStoreDrugAccountDetail> l = criteria.list();
		return l;
	}

	public InventoryStoreDrugAccountDetail saveStoreDrugAccountDetail(
			InventoryStoreDrugAccountDetail StoreDrugAccountDetail)
			throws DAOException {
		return (InventoryStoreDrugAccountDetail) sessionFactory
				.getCurrentSession().merge(StoreDrugAccountDetail);
	}

	public InventoryStoreDrugAccountDetail getStoreDrugAccountDetailById(
			Integer id) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreDrugAccountDetail.class,
						"billDetail").add(Restrictions.eq("billDetail.id", id));

		return (InventoryStoreDrugAccountDetail) criteria.uniqueResult();
	}

	// ghanshyam 15-june-2013 New Requirement #1636 User is able to see and dispense drugs in patient queue for issuing drugs, as ordered from dashboard
	public List<PatientSearch> searchListOfPatient(Date date, String searchKey,
			int page) throws DAOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(date) + " 00:00:00";
		String endDate = sdf.format(date) + " 23:59:59";
		String hql = "SELECT DISTINCT ps from PatientSearch ps,OpdDrugOrder o INNER JOIN o.patient p where ps.patientId=p.patientId " +
		" AND o.createdOn BETWEEN '"+ startDate+ "' AND '" + endDate + "' " +
		" AND o.orderStatus=0 AND o.cancelStatus=0 " +
		" AND (ps.identifier LIKE '%" 	+ searchKey + "%' OR ps.fullname LIKE '" + searchKey + "%')";
		int firstResult = (page - 1) * InventoryConstants.PAGESIZE;
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery(hql);
		List<PatientSearch> list = q.list();
		return list;
	}

        //  to work with size Selector
	public List<PatientSearch> searchListOfPatient(Date date, String searchKey,
			int page,int pgSize) throws DAOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(date) + " 00:00:00";
		String endDate = sdf.format(date) + " 23:59:59";
		String hql = "SELECT DISTINCT ps from PatientSearch ps,OpdDrugOrder o INNER JOIN o.patient p where ps.patientId=p.patientId " +
		" AND o.createdOn BETWEEN '"+ startDate+ "' AND '" + endDate + "' " +
		" AND o.orderStatus=0 AND o.cancelStatus=0 " +
		" AND (ps.identifier LIKE '%" 	+ searchKey + "%' OR ps.fullname LIKE '" + searchKey + "%')";
		int firstResult = (page - 1) * pgSize;
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery(hql).setMaxResults(pgSize).setFirstResult(firstResult);
		List<PatientSearch> list = q.list();
		return list;
	}

         //  to work with size Selector
	public int countSearchListOfPatient(Date date, String searchKey,
			int page) throws DAOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(date) + " 00:00:00";
		String endDate = sdf.format(date) + " 23:59:59";
		String hql = "SELECT DISTINCT ps from PatientSearch ps,OpdDrugOrder o INNER JOIN o.patient p where ps.patientId=p.patientId " +
		" AND o.createdOn BETWEEN '"+ startDate+ "' AND '" + endDate + "' " +
		" AND o.orderStatus=0 AND o.cancelStatus=0 " +
		" AND (ps.identifier LIKE '%" 	+ searchKey + "%' OR ps.fullname LIKE '" + searchKey + "%')";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery(hql);
		List<PatientSearch> list = q.list();
		return list.size();
	}

	public List<OpdDrugOrder> listOfOrder(Integer patientId, Date date)
			throws DAOException {
		/*
		 * Criteria criteria =
		 * sessionFactory.getCurrentSession().createCriteria(
		 * OpdDrugOrder.class); criteria.add(Restrictions.eq("patient",
		 * patient)); criteria.add(Restrictions.eq("orderStatus", 0));
		 * criteria.add(Restrictions.eq("cancelStatus", 0)); return
		 * criteria.list();
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(date) + " 00:00:00";
		String endDate = sdf.format(date) + " 23:59:59";
		String hql = "from OpdDrugOrder o where o.patient='"
				+ patientId
				+ "' AND o.createdOn BETWEEN '"
				+ startDate
				+ "' AND '"
				+ endDate
				+ "' AND o.orderStatus=0 AND o.cancelStatus=0 GROUP BY encounter";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery(hql);
		List<OpdDrugOrder> list = q.list();
		return list;
	}

	public List<OpdDrugOrder> listOfDrugOrder(Integer patientId,
			Integer encounterId) throws DAOException {
		String hql = "from OpdDrugOrder o where o.patient='" + patientId
				+ "' AND o.encounter='" + encounterId
				+ "' AND o.orderStatus=0 AND o.cancelStatus=0";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery(hql);
		List<OpdDrugOrder> list = q.list();
		return list;
	}

	public OpdDrugOrder getOpdDrugOrder(Integer patientId,Integer encounterId,Integer inventoryDrugId,Integer formulationId) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				OpdDrugOrder.class);
		criteria.add(Restrictions.eq("patient.id", patientId));
		criteria.add(Restrictions.eq("encounter.encounterId", encounterId));
		criteria.add(Restrictions.eq("inventoryDrug.id", inventoryDrugId));
		criteria.add(Restrictions.eq("inventoryDrugFormulation.id", formulationId));

		return (OpdDrugOrder) criteria.uniqueResult();
	}

	/**
	 * InventoryStoreItemPatient
	 */
	public List<InventoryStoreItemPatient> listStoreItemPatient(
			Integer storeId,  Integer receiptId, String name, String fromDate, String toDate,
			int min, int max) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItemPatient.class, "bill")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("bill.store", "store");

		if (storeId != null) {

			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.or(
					Restrictions.like("bill.identifier", "%" + name + "%"),
					Restrictions.like("bill.name", "%" + name + "%")));
		}

		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("listBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		criteria.addOrder(Order.desc("bill.createdOn"));
		if (receiptId != null) {

			criteria.add(Restrictions.eq("bill.id", receiptId));
		}
		criteria.setFirstResult(min).setMaxResults(max);
		List<InventoryStoreItemPatient> l = criteria.list();
		return l;
	}

	public int countStoreItemPatient(Integer storeId, String name,
			String fromDate, String toDate) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InventoryStoreItemPatient.class, "bill")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("bill.store", "store")
				.setProjection(Projections.rowCount());

		if (storeId != null) {
			criteria.add(Restrictions.eq("store.id", storeId));
		}
		if (!StringUtils.isBlank(name)) {
			criteria.add(Restrictions.or(
					Restrictions.like("bill.identifier", "%" + name + "%"),
					Restrictions.like("bill.name", "%" + name + "%")));
		}
		if (!StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
			String startFromDate = fromDate + " 00:00:00";
			String endFromDate = fromDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startFromDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endFromDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = toDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		} else if (!StringUtils.isBlank(fromDate)
				&& !StringUtils.isBlank(toDate)) {
			String startToDate = fromDate + " 00:00:00";
			String endToDate = toDate + " 23:59:59";
			try {
				criteria.add(Restrictions.and(
						Restrictions.ge("bill.createdOn",
								formatter.parse(startToDate)),
						Restrictions.le("bill.createdOn",
								formatter.parse(endToDate))));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("countBill>>Error convert date: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		Number rs = (Number) criteria.uniqueResult();
		return rs != null ? rs.intValue() : 0;
	}

	public InventoryStoreItemPatient saveStoreItemPatient(
			InventoryStoreItemPatient bill) throws DAOException {
		return (InventoryStoreItemPatient) sessionFactory.getCurrentSession()
				.merge(bill);
	}

	public InventoryStoreItemPatient getStoreItemPatientById(Integer id)
			throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InventoryStoreItemPatient.class, "itemPatient");
		criteria.add(Restrictions.eq("patientBill.id", id));
		return (InventoryStoreItemPatient) criteria.uniqueResult();
	}

	/**
	 * InventoryStoreItemPatientDetail
	 */
	public List<InventoryStoreItemPatientDetail> listStoreItemPatientDetail(
			Integer storeItemPatientId) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemPatientDetail.class,
						"billDetail")
				.add(Restrictions.eq("billDetail.storeItemPatient.id",
						storeItemPatientId));
		List<InventoryStoreItemPatientDetail> l = criteria.list();
		return l;
	}

	public InventoryStoreItemPatientDetail saveStoreItemPatientDetail(
			InventoryStoreItemPatientDetail storeItemPatientDetail)
			throws DAOException {
		return (InventoryStoreItemPatientDetail) sessionFactory
				.getCurrentSession().merge(storeItemPatientDetail);
	}

	public InventoryStoreItemPatientDetail getStoreItemPatientDetailById(
			Integer id) throws DAOException {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(InventoryStoreItemPatientDetail.class,
						"billDetail").add(Restrictions.eq("billDetail.id", id));

		return (InventoryStoreItemPatientDetail) criteria.uniqueResult();
	}

	public List<OpdDrugOrder> listOfNotDispensedOrder(Integer patientId, Date date,Encounter encounterId)
	throws DAOException {

		Integer id= encounterId.getEncounterId();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(date) + " 00:00:00";
		String endDate = sdf.format(date) + " 23:59:59";
		String hql = "from OpdDrugOrder o where o.patient='"
				+ patientId
				+ "' AND o.createdOn BETWEEN '"
				+ startDate
				+ "' AND '"
				+ endDate
				+ "' AND o.orderStatus = 0 "
				+ " AND o.encounter = "
				+ id ;
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery(hql);
		List<OpdDrugOrder> list = q.list();
		return list;
		}


}
