
class MyTable {
	private String[] keys;
	private String[] values;
	private int m_size;
	private int m_currSize;

	public MyTable() {
		this(10);
	}

	public MyTable(int size) {
		keys = new String[size];
		values = new String[size];
		m_size = size;
		m_currSize = 0;
	}

	public void put(String key, String value) {
		String[] tmp;

		if(m_currSize >= m_size) {
			tmp = new String[m_size*2];
			for(int i = 0; i < m_size; i++) {
				tmp[i] = keys[i];
			}
			keys = tmp;

			tmp = new String[m_size*2];
			for(int i = 0; i < m_size; i++) {
				tmp[i] = values[i];
			}
			values = tmp;
			m_size *= 2;
		}

		keys[m_currSize] = key;
		values[m_currSize] = value;
		m_currSize++;		
	}

	public int[] get(String key) {
		int[] ret;
		int size = 0;
		int curr = 0;

		for(int i = 0; i < m_currSize; i++) {
			if(keys[i].equals(key)) {
				size++;
			}
		}	

		ret = new int[size];
		for(int i = 0; i < m_currSize; i++) {
			if(keys[i].equals(key)) {
				ret[curr++] = i;
			}
		}

		return ret;
	}

	public String index(int i) {
		return values[i];
	}
}
