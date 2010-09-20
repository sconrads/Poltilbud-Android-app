package no.poltilbud.feed;

import java.net.MalformedURLException;
import java.net.URL;

public class Product implements Comparable<Product>{
	private String name;
	private URL url;
	private String type;
	private String differance;
	private String differancePerc;
	private String newPrice;
	private String oldPrice;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public URL getLink() {
		return url;
	}
	
	public void setLink(String link) {
		try {
			this.url = new URL(link);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public String getType() {
		return type;
	}

	public String getDifferance() {
		return differance;
	}

	public void setDifferance(String differance) {
		this.differance = differance;
	}

	public String getDifferancePerc() {
		return differancePerc;
	}

	public void setDifferancePerc(String differancePerc) {
		this.differancePerc = differancePerc;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public String getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}

	public void setType(String type) {
		this.type = type.trim();
	}

	public Product copy(){
		Product copy = new Product();
		copy.name = name;
		copy.url = url;
		copy.type = type;
		copy.differance = differance;
		copy.differancePerc = differancePerc;
		copy.type = type;
		copy.newPrice = newPrice;
		copy.oldPrice = oldPrice;
		return copy;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Title: ");
		sb.append(name);
		sb.append('\n');
		sb.append("Link: ");
		sb.append(url);
		sb.append('\n');
		sb.append("Type: ");
		sb.append(type);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((differance == null) ? 0 : differance.hashCode());
		result = prime * result
				+ ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (differance == null) {
			if (other.differance != null)
				return false;
		} else if (!differance.equals(other.differance))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(Product another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return another.name.compareTo(name);
	}
}
