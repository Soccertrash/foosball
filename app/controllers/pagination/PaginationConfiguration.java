package controllers.pagination;

import java.util.ArrayList;
import java.util.List;

public class PaginationConfiguration {

	/** The amount of total rows */
	private final int rowAmount;

	/** The page size. */
	private final int pageSize;

	/** The columns that are available (displayable) */
	private final List<Column> columns;

	/** The id column. */
	private final String idColumn;

	private PaginationConfiguration(int rowAmount, int pageSize,
			List<Column> columns, String idColumn) {
		super();
		this.rowAmount = rowAmount;
		this.pageSize = pageSize;
		this.columns = columns;
		this.idColumn = idColumn;
	}

	/**
	 * @return the rowAmount
	 */
	public int getRowAmount() {
		return rowAmount;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return the colums
	 */
	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * @return the idColumn
	 */
	public String getIdColumn() {
		return idColumn;
	}

	/**
	 * The Class Column contains all necessary information to build a column in
	 * the presentation layer
	 */
	public static class Column {

		/** The property name (used to access the actual data). */
		private final String propertyName;

		/** The column header name. */
		private final String columnHeader;
		private final boolean isSortable;

		/**
		 * Instantiates a new column.
		 * 
		 * @param propertyName
		 *            the property name
		 * @param columnHeader
		 *            the column header
		 * @param isSortable
		 *            the is sortable
		 */
		public Column(String propertyName, String columnHeader,
				boolean isSortable) {
			super();
			this.propertyName = propertyName;
			this.columnHeader = columnHeader;
			this.isSortable = isSortable;
		}

		/**
		 * @return the propertyName
		 */
		public String getPropertyName() {
			return propertyName;
		}

		/**
		 * @return the columnHeader
		 */
		public String getColumnHeader() {
			return columnHeader;
		}

		/**
		 * Checks if is sortable.
		 * 
		 * @return the isSortable
		 */
		public boolean isSortable() {
			return isSortable;
		}

	}

	public static class Builder {
		/** The amount of total rows */
		private int rowAmount;

		/** The page size. */
		private int pageSize;

		/** The colums that are available (displayable) */
		private List<Column> colums = new ArrayList<>(10);

		private String idColumn;

		public Builder rowAmount(int rowAmount) {
			this.rowAmount = rowAmount;
			return this;
		}

		public Builder pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public Builder idColumn(String idColumn) {
			this.idColumn = idColumn;
			return this;
		}

		public Builder addColumn(String propertyName, String columnHeader,
				boolean isSortable) {
			this.colums.add(new Column(propertyName, columnHeader, isSortable));
			return this;
		}

		public PaginationConfiguration build() {
			return new PaginationConfiguration(rowAmount, pageSize, colums,
					idColumn);
		}
	}
}
