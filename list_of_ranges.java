import java.lang.Math;

public class RangeList
{
	Node first;  // first node
	int node_count;  // number of nodes/ranges
	int val_count;  // number of individual values

	/** Linked list node to use. */
	class Node {
		int low;
		int high;
		Node prev;
		Node next;
		
		Node (int low, int high, Node prev, Node next)
		{
			this.low = low;
			this.high = high;
			this.prev = prev;
			this.next = next;
		}
	}
	
	public RangeList()
	{
		this.first = null;
		this.node_count = 0;
		this.val_count = 0;
	}
	
	// helper function, not really needed but eh
	public boolean isEmpty()
	{
		return ((node_count == 0) && (val_count == 0));  // just to be safe
	}
	
	/** 
	 * Return the number of distinct ranges in the RangeList.
	 * Performance must be O(N) 
	 */
	public int numberRanges() 
	{ 	
		return node_count;  // this is O(1), is this okay? it wants O(N)
	}
	
	/** 
	 * Return the number of distinct values in the RangeList.
	 * Performance must be O(1) 
	 */
	public int numberValues()
	{
		return val_count;
	}
	
	/**
	 * Determines whether the current RangeList is a subset of the parameter.
	 * This object is a subset of rl if every value contained in this object also is found in rl.
	 */
	public boolean subsetOf(RangeList rl)
	{  // O(N)
		boolean R = false;  // return value
		Node temp = first;  // node to iterate through main RL
		Node temp2 = rl.first;  // node to iterate through arg RL
		
		while((temp != null) && (temp2 != null))
		{
			if(temp2.low > temp.high)
			{  // passed this node
				break;
			}
			if((temp.low >= temp2.low) && (temp.high <= temp2.high))
			{
				temp = temp.next;
			} else
			{
				temp2 = temp2.next;
			}
		}
		if(temp == null)
		{
			R = true;
		}
		
		return R;
	}
	
	/**
	 * Determine if the current RangeList contains the same values as the parameter list.
	 * Performance: O(N)
	 */
	public boolean equals(Object o)
	{
		boolean R = true;  // return value
		Node temp = first;
		Node temp2 = ((RangeList) o).first;
		
		while((temp != null) && (temp2 != null))
		{
			if((temp.low != temp2.low) || (temp.high != temp2.high))
			{
				R = false;
				break;
			}
			temp = temp.next;
			temp2 = temp2.next;
		}
		
		return R;
	}
	
	
	/**
	 * Returns true if the value is contained in the given RangeList.
	 * Performance must be O(N)
	 */
	public boolean contains(int value) {
		boolean R = false;  // return value
		Node temp = first;
		
		while(temp != null)
		{
			if(temp.low > value)
			{  // no need to keep going
				break;
			}
			if((value >= temp.low) && (value <= temp.high))
			{
				R = true;
				break;
			}
			temp = temp.next;
		}
		
		return R;
	}
	
	/**
	 * If value does not already exist in RangeList, add it and return true, otherwise return false.
	 * 
	 * This method must guarantee the GAP property and the ASCENDING property of RangeList
	 * Performance must be O(N)
	 */
	public boolean add(int value)
	{
		boolean R = false;  // return value
		
		if(!contains(value))
		{  // if value not already in rangelist
			if(isEmpty())
			{  // if adding first val
				first = new Node(value, value, null, null);
				node_count++;
			} else
			{
				Node temp = first;  // node to iterate
				Node last = first;  // will end up being last node
				boolean below = false;  // flag for if value is one below a range
				boolean above = false;  // flag for if value is one above a range
				boolean added = false;  // flag to check if value added
				
				while(temp != null)
				{
					if(value < temp.low)
					{
						below = (value == (temp.low - 1));
						
						if(temp.prev != null)
						{
							above = (value == (temp.prev.high + 1));
						}
						if(below && !above)
						{  // one below
							temp.low = value;
							added = true;
						} else if(!below && above)
						{  // one above
							temp.prev.high = value;
							added = true;
						} else if(below && above)
						{  // fills one-wide gap between two ranges
							temp.prev.high = temp.high;
							temp.prev.next = temp.next;
							if(temp.next != null)
							{
								temp.next.prev = temp.prev;
							}
							added = true;
							node_count--;
						} else
						{  // value in gap not bordering any other ranges
							Node insert = new Node(value, value, temp.prev, temp);
							
							if(temp.prev != null)
							{
								temp.prev.next = insert;
							} else
							{  // first node
								first = insert;
							}
							temp.prev = insert;
							added = true;
							node_count++;
						}
						if(added)
						{  // once value is added, move on
							break;
						}
						
					}
					last = temp;
					temp = temp.next;
					below = false;
					above = false;
				}
				if(!added)
				{  // if not already added, add to end
					if(value == (last.high + 1))
					{
						last.high = value;
					} else
					{
						temp = new Node(value, value, last, null);
						last.next = temp;
						node_count++;
					}
				}
			}
			val_count++;
			R = true;
		}
		
		return R;
	}

	/**
	 * Remove value from RangeList if it exists (and return true) otherwise return false.
	 * 
	 * This method must guarantee the GAP property and the ASCENDING property of RangeList
	 * Performance must be O(N)
	 */
	public boolean remove(int value)
	{
		boolean R = false;  // return value
		
		if(contains(value))
		{
			Node temp = first;
			boolean low = false;  // flag if value is low of a range
			boolean high = false;  // flag if value is high of a range
			
			while(temp != null)
			{
				if((value >= temp.low) && (value <= temp.high))
				{  // in current range
					low = (value == temp.low);
					high = (value == temp.high);
					if(low && !high)
					{
						temp.low = (value + 1);
						break;
					} else if(!low && high)
					{
						temp.high = (value - 1);
						break;
					} else if(low && high)
					{  // one-wide node
						if(temp.prev != null)
						{
							temp.prev.next = temp.next;
						}
						if(temp.next != null)
						{
							temp.next.prev = temp.prev;
						}
						node_count--;
						break;
					} else
					{  // somewhere in the middle
						Node low_node = new Node(temp.low, (value - 1), temp.prev, null);
						Node high_node = new Node((value + 1), temp.high, low_node, temp.next); 
						low_node.next = high_node;

						node_count++;
						if(temp == first)
						{
							first = low_node;
						} else
						{
							if(temp.prev != null)
							{
								temp.prev.next = low_node;
							}
							if(temp.next != null)
							{
								temp.next.prev = high_node;
							}
							break;
						}
					}
				}
				temp = temp.next;
			}
			val_count--;
			if(val_count == 0)
			{
				first = null;
			}
			R = true;
		}
			
		return R;
	}

	/** 
	 * Produce string representation of ranges, in order from least to greatest.
	 * Performance must be O(N) 
	 */
	public String toString()
	{
		String R = "[";  // return value
		Node temp = first;
		
		while(temp != null)
		{
			if(temp.low != temp.high)
			{
				R += (temp.low + "-" + temp.high + ", ");
			} else
			{
				R += (temp.low + ", ");
			}
			temp = temp.next;
		}
		R = R.substring(0, (R.length() - 2));
		R += "]";
		
		return R;
	}

	// BONUS QUESTIONS
	// --------------------------------------------------
	/** Return a random integer from this RangeList uniformly over all values. */
	public int random()
	{
		int R = 0;  // return value
		int node_id = (int) ((1000 * Math.random()) % node_count);
		Node temp = first;
		
		for(int i = 0; i < node_id; i++)
		{
			temp = temp.next;
		}
		int val_id = (int) ((1000 * Math.random()) % (temp.high - temp.low + 1));
		
		R = temp.low;
		for(int i = 0; i < val_id; i++)
		{
			R++;
		}
		
		return R;
	}
	public java.util.Iterator<Integer> iterator() {
		throw new RuntimeException("ONLY COMPLETE IF DOING BONUS");
	}

	
	public static void main(String[] args) {}
}
