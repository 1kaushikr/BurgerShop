import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class MMBurgers implements MMBurgersInterface {
	int M ;
	int r = 0;
	int K;
	int T = 0;
	inspend inspand = new inspend();
	Vector<Queue> Counters = new Vector<>();
	Heap1 QuNos = new Heap1();
	Customerstree Tree = new Customerstree();
	LinkedList<customer> avgtim = new LinkedList<>();
	LinkedList<order> PendOrders = new LinkedList<>();
	LinkedList<griddle> Griddle = new LinkedList<>();

	public order fstorder()
	{
		LinkedList<order> m = new LinkedList<>();
		order o = PendOrders.remove();
		m.add(o);
		while(PendOrders.size()>0)
		{
			order f = PendOrders.remove();
			if (f.timplcd1()<o.timplcd1())
			{
				o = f;
			}
			else if (f.timplcd1()==o.timplcd1())
			{
				if (f.cu1().Counter1() > o.cu1().Counter1())
				{
					o = f;
				}
			}
			m.add(f);
		}
		PendOrders = m;
		return o;
	}

	class inspend
	{
		order o;
		int numb;
		public order o1()
		{
			return o;
		}
		public int numb1()
		{
			return numb;
		}
		public inspend()
		{
			this.numb = 0;
			this.o = null;
		}
	}

	class order
	{
		int timplcd;
		customer cu;
		public int timplcd1()
		{
			return timplcd;
		}
		public customer cu1()
		{
			return cu;
		}
	}

	public void tranfer(int t)
	{
		for (int i = 0; i < Counters.size() ; i++)
		{
			if ( Counters.get(i).size() > 0)
			{
				customer c = (customer)  Counters.get(i).peek();
				if ((c.queuewait1() + c.EntryTime1())==t)
				{
					Counters.get(i).remove();
					order o = new order();
					o.cu = c;
					o.timplcd = t;
					PendOrders.add(o);
					r = r + o.cu1().NoBurger1();
					QuNos.delete( Counters.get(i).size()+1,i+1);
					QuNos.Insert( Counters.get(i).size(),i+1);
				}
			}
		}
	}

	public void griddling(int t)
	{
		while (Griddle.size()>0)
		{
			if (t==(Griddle.peek().timput + 10))
			{
				griddle c = Griddle.remove();
				c.order1().cu1().gotburgers = c.order1().cu1().gotburgers1() +  1;
				if (c.order1().cu1().gotburgers1() == c.order1().cu1().NoBurger1())
				{
					c.order1().cu1().ordcomtim = t + 1;
				}
			}
			else
			{
				break;
			}
		}

		while (Griddle.size()<M)
		{
			if (inspand.numb > 0)
			{
				griddle g = new griddle();
				g.timput = t;
				g.or = inspand.o1();
				inspand.numb = inspand.numb1() - 1;
				Griddle.add(g);
			}
			else if(PendOrders.size()>0)
			{
				order y = fstorder();
				if (t>=y.timplcd)
				{
					PendOrders.remove(y);
					inspand.o = y;
					griddle g = new griddle();
					g.timput = t;
					g.or = y;
					r = r - y.cu1().NoBurger1();
					inspand.numb = y.cu1().NoBurger1() - 1;
					Griddle.add(g);
				}
				else
				{
					break;
				}
			}
			else
			{
				break;
			}
		}
	}

	class griddle
	{
		int timput;
		order or;
		public int timput1()
		{
			return timput;
		}
		public order order1()
		{
			return or;
		}
	}

	class customer
	{
		int EntryTime;
		int queuewait;
		int id;
		int Counter;
		int NoBurger;
		int gotburgers;
		int ordcomtim;

		public int NoBurger1()
		{
			return 	NoBurger;
		}
		public int id1()
		{
			return id;
		}
		public int queuewait1()
		{
			return queuewait;
		}
		public int Counter1()
		{
			return Counter;
		}
		public int EntryTime1()
		{
			return EntryTime;
		}
		public int gotburgers1()
		{
			return gotburgers;
		}
		public int ordcomtim1()
		{
			return ordcomtim;
		}
		public customer()
		{
			this.gotburgers = 0;
		}
	}

	public boolean isEmpty()
	{
		if (PendOrders.size()==0 && inspand.numb==0 && Griddle.size() == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	} 

	public void setK(int k) throws IllegalNumberException
	{
		if (k<=0)
		{
			throw new IllegalNumberException("Invalid Counter Number");
		}
		else
		{
			for (int i = 0; i < k ; i++ )
			{
				QuNos.Insert(0, i+1);
			}
			for (int i = 0; i < k ; i++ )
			{
				Queue<customer> q = new LinkedList<>();
				Counters.add(i,q);
			}
			K = k;
		}
	}   

	public void setM(int m) throws IllegalNumberException
	{
		if (m<=0)
		{
			throw new IllegalNumberException("Invalid Counter Number");
		}
		else
		{
			M = m;
		}
	} 

	public void advanceTime(int t) throws IllegalNumberException
	{
		if (t<T)
		{
			throw new IllegalNumberException("Wrong Order or Time0");
		}
		else
		{
			for(int i = T; i<=t; i++)
			{
				tranfer(i);
				griddling(i);
			} 
			T = t;
		}
	}

	public void arriveCustomer(int id, int t, int numb) throws IllegalNumberException
	{
		if (numb<=0 || t<T)
		{
			throw new IllegalNumberException("Wrong Order or Time1");
		}
		else
		{
			for(int i = T; i<=t; i++)
			{
				tranfer(i);
				griddling(i);
			} 
			T = t;
			int[] j = QuNos.Deletemin();
			QuNos.Insert(j[0] + 1,j[1]);
			customer c = new customer();
			c.id = id;
			c.Counter = j[1];
			c.EntryTime = t;
			c.NoBurger = numb;
			Counters.get(j[1]-1).add(c);
			Tree.addcustomer(c);
			avgtim.add(c);
			if (j[0]==0)
			{
				c.queuewait = j[1];
			}
			else
			{
				int h = Counters.get(j[1]-1).size();
				customer d = (customer) Counters.get(j[1]-1).peek();
				c.queuewait = d.queuewait - (t - d.EntryTime) + j[1]*h;
			}
		}
	} 


	public int customerState(int id, int t) throws IllegalNumberException
	{
		if (t<T)
		{
			throw new IllegalNumberException("Wrong Order or Time2");
		}
		else
		{
			for(int i = T; i<=t; i++)
			{
				tranfer(i);
				griddling(i);
			} 
			T = t;
			customer c = new customer();
			c =  Tree.search(id);
			if (c==null)
			{
				return 0;
			}
			else if(c.EntryTime1()<=t && t<c.queuewait1() + c.EntryTime1())
			{
				return c.Counter;
			}
			else if ( c.ordcomtim1() == 0 || t < c.ordcomtim1())
			{
				return K + 1;
			}
			else
			{
				return K + 2;
			}
		}
	} 

	public int griddleState(int t) throws IllegalNumberException
	{
		if (t<T)
		{
			throw new IllegalNumberException("Wrong Order or Time3");
		}
		else
		{
			for(int i = T; i<=t; i++)
			{
				tranfer(i);
				griddling(i);
			} 
			T = t;
			return Griddle.size();
		}
	} 

	public int griddleWait(int t) throws IllegalNumberException
	{
		if (t<T)
		{
			throw new IllegalNumberException("Wrong Order or Time4");
		}
		else
		{
			for(int i = T; i<=t; i++)
			{
				tranfer(i);
				griddling(i);
			} 
			T = t;
			int m = inspand.numb + r;
			return m;
		}
	} 

	public int customerWaitTime(int id) throws IllegalNumberException
	{
		customer c = Tree.search(id);
		if (c==null)
		{
			throw new IllegalNumberException("This Customer doesn't come");
		}
		else
		{
			return (c.ordcomtim - c.EntryTime);
		}
	} 

	public float avgWaitTime()
	{
		int counter = 0;
		int time = 0;
		while (avgtim.size()>0)
		{
			customer c = avgtim.remove();
			time  = time + (c.ordcomtim - c.EntryTime);
			counter = counter + 1;
		}
		return (float)time/counter;
	}

	public class Heap1
	{
		public int[][] s = new int[2][3];
		public int N = 0;

		public int find(int i, int j)
		{
			int k = -1;
			for (int p = 1; p<=N;  p++)
			{
				if (s[0][p] == i && s[1][p] == j)
				{
					k = p;
				}
			}
			return k;
		}

		public void delete(int i, int j) 
		{
			int l = find(i,j);
			int b = s[0][N];
			int d = s[1][N];
			s[0][N] = 0;
			s[1][N] = 0;
			PercDown(l,b,d);
			N = N - 1;
		}

		public int[] Deletemin() throws IllegalNumberException
		{
			if (N==0)
			{
				throw new IllegalNumberException("Heap is Empty");
			} 
			int[] IDNum = new int[2];
			int a = s[0][1];
			int b = s[0][N];
			int c = s[1][1];
			int d = s[1][N];
			s[0][N] = 0;
			s[1][N] = 0;
			PercDown(1,b,d);
			N = N - 1;
			IDNum[0] = a;
			IDNum[1] = c;
			return IDNum;
		}

		public void Insert(int o, int p)
		{
			if (N==0)
			{
				s[0][1]  =  o;
				s[1][1]  =  p;
				N = 1;
			}
			else if (s[0].length <= 2*N + 2)
			{
				int[][] ar1 =  new int[2][2*(s[0].length)];
				int counter = 1;
				while (counter<=N)
				{
					ar1[0][counter] = s[0][counter];
					ar1[1][counter] = s[1][counter];
					counter = counter + 1;
				}
				s = ar1;
				PercUp((1+N),o,p);
				N = N + 1;
			}
			else
			{
				PercUp((1+N),o,p);
				N = N + 1;
			}
		}


		public void PercUp(int k, int l, int m)
		{
			if (k==1)
			{
				s[0][k] = l;
				s[1][k] = m; 
			}
			else if (s[0][k/2] > l)
			{
				s[0][k] = s[0][k/2];
				s[1][k] = s[1][k/2];
				PercUp(k/2,l,m);
			}
			else if (s[0][k/2] == l)
			{
				if(s[1][k/2] > m)
				{
					s[0][k] = s[0][k/2];
					s[1][k] = s[1][k/2];
					PercUp(k/2,l,m);
				}
				else
				{
					s[0][k] = l;
					s[1][k] = m;
				}
			}
			else
			{
				s[0][k] = l;
				s[1][k] = m;
			}
		}

		public void PercDown(int k, int p, int q)
		{
			if (s[1][2*k] != 0 && s[1][2*k + 1] != 0)
			{

				if (s[0][2*k] < s[0][2*k + 1])
				{
					if (s[0][2*k  + 1]  == p)
					{
						s[0][k] = s[0][2*k];
						s[1][k] = s[1][2*k];
						PercDown(2*k,p,q);
					}
					else if (s[0][2*k]  == p)
					{
						if(s[1][2*k ] < q)
						{
							s[0][k] = s[0][2*k];
							s[1][k] = s[1][2*k];
							PercDown(2*k ,p,q);
						}
						else
						{
							s[0][k] = p;
							s[1][k] = q;
						}
					}
					else if (s[0][2*k]  < p)
					{
						s[0][k] = s[0][2*k];
						s[1][k] = s[1][2*k];
						PercDown(2*k,p,q);
					}
					else 
					{
						s[0][k] = p;
						s[1][k] = q;
					}
				}	
				else if (s[0][2*k] == s[0][2*k + 1])
				{
					if (s[0][2*k] > p)
					{
						s[0][k] = p;
						s[1][k] = q;
					}
					else if (s[0][2*k] == p)
					{
						if (s[1][2*k] > s[1][2*k + 1])
						{
							if (q < s[1][2*k + 1])
							{
								s[0][k] = p;
								s[1][k] = q;
							}
							else
							{
								s[0][k] = s[0][2*k + 1];
								s[1][k] = s[1][2*k + 1];
								PercDown(2*k + 1,p,q);
							}
						}
						else
						{
							if (q < s[1][2*k])
							{
								s[0][k] = p;
								s[1][k] = q;
							}
							else
							{
								s[0][k] = s[0][2*k];
								s[1][k] = s[1][2*k];
								PercDown(2*k,p,q);
							}
						}
					}
					else
					{
						if (s[1][2*k] > s[1][2*k + 1])
						{
							s[0][k] = s[0][2*k + 1];
							s[1][k] = s[1][2*k + 1];
							PercDown(2*k + 1,p,q);
						}
						else
						{
							s[0][k] = s[0][2*k];
							s[1][k] = s[1][2*k];
							PercDown(2*k,p,q);
						}
					}
				}
				else
				{
					if (s[0][2*k]  == p)
					{
						s[0][k] = s[0][2*k + 1];
						s[1][k] = s[1][2*k + 1];
						PercDown(2*k + 1 , p,q);
					}
					else if (s[0][2*k+1]  == p)
					{
						if(s[1][2*k + 1] < q)
						{
							s[0][k] = s[0][2*k + 1];
							s[1][k] = s[1][2*k + 1];
							PercDown(2*k + 1,p,q);
						}
						else
						{
							s[0][k] = p;
							s[1][k] = q;
						}
					}
					else if (s[0][2*k + 1]  < p)
					{
						s[0][k] = s[0][2*k + 1];
						s[1][k] = s[1][2*k + 1];
						PercDown(2*k + 1,p,q);
					}
					else 
					{
						s[0][k] = p;
						s[1][k] = q;
					}
				}
			}
			else if (s[1][2*k] != 0 && s[1][2*k + 1] == 0)
			{
				if (s[0][2*k] < p)
				{
					s[0][k] = s[0][2*k];
					s[1][k] = s[1][2*k];
					s[0][2*k] = p;
					s[1][2*k] = q;
				}
				else if(s[0][2*k] == p)
				{
					if(s[1][2*k] < q)
					{
						s[0][k] = s[0][2*k];
						s[1][k] = s[1][2*k];
						s[0][2*k] = p;
						s[1][2*k] = q;
					}
					else
					{
						s[0][k] = p;
						s[1][k] = q;
					}
				}
				else
				{
					s[0][k] = p;
					s[1][k] = q;
				}
			}
			else
			{
				s[0][k] = p;
				s[1][k] = q;
			}
		}
	}

	public class Node
	{
		customer E1;
		customer E2;
		customer E3;
		Node parent;
		Vector<Node> Children;
		public Node parent1()
		{
			return parent;
		}
		public Vector<Node> children1()
		{
			return Children;
		}
		public customer E11()
		{
			return E1;
		}
		public customer E21()
		{
			return E2;
		}
		public customer E31()
		{
			return E3;
		}
		public Node()
		{
			this.parent = null;
			this.Children = null;
		}
	}

	public class Customerstree
	{ 
		Node root = new Node();
		int N = 0;

		public customer search(int id)
		{
			return givNod(id,root);
		}

		public customer givNod(int id,Node n)
		{
			int u  = Size(n);
			if (n.Children != null)
			{
				Vector<Node> v = new Vector<Node>();
				v = n.Children;
				if (u == 3)
				{
					if (id<n.E1.id)
					{

						return givNod(id, v.get(0));
					}
					else if (id == n.E1.id)
					{
						return n.E1;
					}
					else if (n.E1.id<id && id<n.E2.id)
					{
						return givNod(id, v.get(1));
					}
					else if (id == n.E2.id)
					{
						return n.E2;
					}
					else if (n.E2.id<id && id<n.E3.id)
					{
						return givNod(id, v.get(2));
					}
					else if (id == n.E3.id)
					{
						return n.E3;
					}
					else
					{
						return givNod(id, v.get(3));
					}
				}
				else if (u == 2)
				{
					if (id<n.E1.id)
					{
						return givNod(id, v.get(0));
					}
					else if (id == n.E1.id)
					{
						return n.E1;
					}
					else if (n.E1.id<id && id<n.E2.id)
					{
						return givNod(id, v.get(1));
					}
					else if (id == n.E2.id)
					{
						return n.E2;
					}
					else
					{
						return givNod(id, v.get(2));
					}
				}
				else
				{
					if (id<n.E1.id)
					{
						return givNod(id, v.get(0));
					}
					else if (id == n.E1.id)
					{
						return n.E1;
					}
					else
					{
						return givNod(id, v.get(1));
					}
				}
			}
			else
			{
				if (u == 3)
				{

					if (id == n.E1.id)
					{
						return n.E1;
					}
					else if (id == n.E2.id)
					{
						return n.E2;
					}
					else if (id == n.E3.id)
					{
						return n.E3;
					}
				}
				else if (u == 2)
				{
					if (id == n.E1.id)
					{
						return n.E1;
					}
					else if (id == n.E2.id)
					{
						return n.E2;
					}
				}
				else
				{
					if (id == n.E1.id)
					{
						return n.E1;
					}
				}
			}
			return null;
		}

		public int Size(Node n)
		{
			int k = 0;
			customer n1 = n.E11();
			customer n2 = n.E21();
			customer n3 = n.E31();
			if (n1 != null)
			{
				k = k + 1;
			}
			if (n2 != null)
			{
				k = k + 1;
			}
			if (n3 != null)
			{
				k = k + 1;
			}
			return k;
		}

		public Node fitNode(int id, Node n)
		{
			int u = Size(n);
			if (n.Children == null)
			{
				return n;
			}
			else
			{
				if (u == 1)
				{
					if (id<n.E1.id)
					{
						return fitNode(id,n.Children.get(0));
					}
					else
					{
						return fitNode(id,n.Children.get(1));
					}
				}
				else if (u == 2)
				{
					if (id<n.E1.id)
					{
						return fitNode(id,n.Children.get(0));
					}
					else if (n.E1.id<id && id<n.E2.id)
					{
						return fitNode(id,n.Children.get(1));
					}
					else
					{
						return fitNode(id,n.Children.get(2));
					}
				}
				else  
				{	
					if (id<n.E1.id)
					{
						return fitNode(id,n.Children.get(0));
					}
					else if (n.E1.id<id && id<n.E2.id)
					{
						return fitNode(id,n.Children.get(1));
					}
					else if (n.E2.id<id && id<n.E3.id)
					{
						return fitNode(id,n.Children.get(2));
					}
					else
					{
						return fitNode(id,n.Children.get(3));
					}
				}
			}
		}

		public void insert(customer n1, Node n)
		{
			if (Size(n) == 0)
			{
				n.E1 = n1;
			}
			else if (Size(n) == 1)
			{
				if (n.E1.id>n1.id)
				{
					n.E2 = n.E1;
					n.E1 = n1;
				}
				else
				{
					n.E2 = n1;
				}
			}
			else if (Size(n) == 2)
			{
				if (n.E1.id>n1.id)
				{
					n.E3 = n.E2;
					n.E2 = n.E1;
					n.E1 = n1;
				}
				else if (n1.id>n.E1.id && n1.id<n.E2.id)
				{
					n.E3 = n.E2;
					n.E2= n1;
				}
				else
				{
					n.E3 = n1;
				}
			}
			else
			{
				if (n == root)
				{
					Node X = new Node();
					Node Y = new Node();
					Node Z = new Node();
					if (n1.id<root.E1.id)
					{
						X.E1 = root.E1;
						Y.E1 = n1;
						Z.E1 = root.E2;
						Z.E2 = root.E3;
					}
					else if (root.E1.id < n1.id && n1.id < root.E2.id)
					{
						X.E1 = n1;
						Y.E1 = root.E1;
						Z.E1 = root.E2;
						Z.E2 = root.E3;
					}
					else if (root.E2.id < n1.id && n1.id < root.E3.id)
					{
						X.E1 = root.E2;
						Y.E1 = root.E1;
						Z.E1 = n1;
						Z.E2 = root.E3;
					}
					else
					{
						X.E1 = root.E2;
						Y.E1 = root.E1;
						Z.E1 = root.E3;
						Z.E2 = n1;
					}
					Vector<Node> v = new Vector<Node>();
					v.add(0,Y);
					v.add(1,Z);
					X.Children =  v;
					Y.parent = X;
					Z.parent = X;
					if (root.Children == null)
					{
						root = X;
					}
					else
					{
						Vector<Node> u = new Vector<Node>();
						u.add(0,root.Children.get(0));
						root.Children.get(0).parent = Y;
						u.add(1,root.Children.get(1));
						root.Children.get(1).parent = Y;
						Vector<Node> w = new Vector<Node>();
						w.add(0,root.Children.get(2));
						root.Children.get(2).parent = Z;
						w.add(1,root.Children.get(3));
						root.Children.get(3).parent = Z;
						w.add(2,root.Children.get(4));
						root.Children.get(4).parent = Z;
						Y.Children = u;
						Z.Children = w;
						root = X;
					}
				}
				else if(n.Children==null)
				{ 
					int j = n.parent.Children.indexOf(n);
					Node Y = new Node();
					Node Z = new Node();
					if (n1.id<n.E1.id)
					{
						Y.E1 = n1;
						Z.E1 = n.E2;
						Z.E2 = n.E3;
						Y.parent = n.parent;
						Z.parent = n.parent;
						n.parent.Children.set(j, Y);;
						n.parent.Children.add(j+1,Z);
						insert(n.E1,n.parent);
					}
					else if (n.E1.id < n1.id && n1.id < n.E2.id)
					{
						Y.E1 = n.E1;
						Z.E1 = n.E2;
						Z.E2 = n.E3;
						Y.parent = n.parent;
						Z.parent = n.parent;
						n.parent.Children.set(j,Y);
						n.parent.Children.add(j+1,Z);
						insert(n1,n.parent);
					}
					else if (n.E2.id < n1.id && n1.id < n.E3.id)
					{
						Y.E1 = n.E1;
						Z.E1 = n1;
						Z.E2 = n.E3;
						Y.parent= n.parent;
						Z.parent = n.parent;
						n.parent.Children.set(j,Y);
						n.parent.Children.add(j+1,Z);
						insert(n.E2,n.parent);
					}
					else
					{
						Y.E1 = n.E1;
						Z.E1 = n.E3;
						Z.E2 = n1;
						Y.parent = n.parent;
						Z.parent = n.parent;
						n.parent.Children.set(j,Y);
						n.parent.Children.add(j+1,Z);
						insert(n.E2,n.parent);
					}
				}
				else
				{  
					int j = n.parent.Children.indexOf(n);
					Node Y = new Node();
					Node Z = new Node();
					if (n1.id<n.E1.id)
					{
						Y.E1 = n1;
						Z.E1 = n.E2;
						Z.E2 = n.E3;
						Vector<Node> p = new Vector<Node>();
						p.add(0,n.Children.get(0));
						n.Children.get(0).parent = Y;
						p.add(1,n.Children.get(1));
						n.Children.get(1).parent = Y;
						Vector<Node> q = new Vector<Node>();
						q.add(0,n.Children.get(2));
						n.Children.get(2).parent = Z;
						q.add(1,n.Children.get(3));
						n.Children.get(3).parent = Z;
						q.add(2,n.Children.get(4));
						n.Children.get(4).parent = Z;
						Y.Children = p;
						Z.Children = q;
						n.parent.Children.set(j,Y);
						n.parent.Children.add(j+1,Z);
						Y.parent = n.parent1();
						Z.parent = n.parent1();
						insert(n.E1,n.parent);
					}
					else if (n.E1.id < n1.id && n1.id < n.E2.id)
					{
						Y.E1 = n.E1;
						Z.E1 = n.E2;
						Z.E2 = n.E3;
						Vector<Node> p = new Vector<Node>();
						p.add(0,n.Children.get(0));
						n.Children.get(0).parent = Y;
						p.add(1,n.Children.get(1));
						n.Children.get(1).parent = Y;
						Vector<Node> q = new Vector<Node>();
						q.add(0,n.Children.get(2));
						n.Children.get(2).parent = Z;
						q.add(1,n.Children.get(3));
						n.Children.get(3).parent = Z;
						q.add(2,n.Children.get(4));
						n.Children.get(4).parent = Z;
						Y.Children = p;
						Z.Children = q;
						n.parent.Children.set(j,Y);
						n.parent.Children.add(j+1,Z);
						Y.parent = n.parent1();
						Z.parent = n.parent1();
						insert(n1,n.parent);
					}
					else if (n.E2.id < n1.id && n1.id < n.E3.id)
					{
						Y.E1 = n.E1;
						Z.E1 = n1;
						Z.E2 = n.E3;
						Vector<Node> p = new Vector<Node>();
						p.add(0,n.Children.get(0));
						n.Children.get(0).parent = Y;
						p.add(1,n.Children.get(1));
						n.Children.get(1).parent = Y;
						Vector<Node> q = new Vector<Node>();
						q.add(0,n.Children.get(2));
						n.Children.get(2).parent = Z;
						q.add(1,n.Children.get(3));
						n.Children.get(3).parent = Z;
						q.add(2,n.Children.get(4));
						n.Children.get(4).parent = Z;
						Y.Children = p;
						Z.Children = q;
						n.parent.Children.set(j,Y);
						n.parent.Children.add(j+1,Z);
						Y.parent = n.parent1();
						Z.parent = n.parent1();
						insert(n.E2,n.parent);
					}
					else
					{
						Y.E1 = n.E1;
						Z.E1 = n.E3;
						Z.E2 = n1;
						Vector<Node> p = new Vector<Node>();
						p.add(0,n.Children.get(0));
						n.Children.get(0).parent = Y;
						p.add(1,n.Children.get(1));
						n.Children.get(1).parent = Y;
						Vector<Node> q = new Vector<Node>();
						q.add(0,n.Children.get(2));
						n.Children.get(2).parent = Z;
						q.add(1,n.Children.get(3));
						n.Children.get(3).parent = Z;
						q.add(2,n.Children.get(4));
						n.Children.get(4).parent = Z;
						Y.Children = p;
						Z.Children = q;
						n.parent.Children.set(j,Y);
						n.parent.Children.add(j+1,Z);
						Y.parent = n.parent1();
						Z.parent = n.parent1();
						insert(n.E2,n.parent);
					}
				}
			}
		}

		public void addcustomer(customer c)
		{
			Node m = fitNode(c.id,root);
			insert(c,m);
			N = N + 1;
		} 
	}
}