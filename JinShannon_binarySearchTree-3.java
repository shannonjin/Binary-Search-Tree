/*Shannon Jin
 * Turner Period 4
 * Turned in 11/20/2015
 * A Binary Search Tree with TreeNodes of type comparable
 */
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class JinShannon_binarySearchTree<E extends Comparable<E>> {

	private TreeNode root;

	public class TreeNode{
		private E data;
		private TreeNode left;
		private TreeNode right;
		private int occurrences;
		private int height;

		public TreeNode(E d, TreeNode l, TreeNode r, int o, int h){

			data=d;
			left=l;
			right=r;
			occurrences=o;
			height=h;
		}
	}

	private TreeNode rotateLL(TreeNode r){

		TreeNode moveUp=r.left;


		r.left=moveUp.right;
		moveUp.right=r;

		int rootHeight=r.height;
		r.height=moveUp.height-moveUp.left.height;
		moveUp.height=rootHeight;

		return moveUp;
	}

	private TreeNode rotateRR(TreeNode r){

		TreeNode moveUp=r.right;
		r.right=moveUp.left;
		moveUp.left=r;

		int rootHeight=r.height;
		r.height=moveUp.height-moveUp.right.height;
		moveUp.height=rootHeight;

		return moveUp;
	}

	private TreeNode rotateLR(TreeNode r){

		TreeNode moveUp=r.left.right;
		r.left.right=moveUp.left;
		moveUp.right=r;
		moveUp.left=r.left;
		moveUp.left=r.left;
		r.left=null;


		moveUp.left.height=moveUp.height;
		moveUp.height=r.height-1;
		r.height=1;

		return moveUp;
	}

	private TreeNode rotateRL(TreeNode r){

		TreeNode moveUp=r.right.left;
		r.right.left=moveUp.right;
		moveUp.left=r;
		moveUp.right=r.right;
		r.right=null;

		moveUp.height=r.height;
		moveUp.left.height--;
		r.height=1;

		moveUp.right.height=moveUp.height;
		moveUp.height=r.height-1;
		r.height=1;

		return moveUp;
	}

	//takes in a subtree that should be balanced and calls the appropriate rotation method based off of subtree height differentials
	private TreeNode balance(TreeNode r){

		int hDiff=r.right.height-r.left.height;

		//left heavy
		if(hDiff<-1){

			if(r.left.left==null){ //CAN YOU DIFFERENTIATE LL AND LR BY HEIGHT DIFFS.?

				r=rotateLR(r);
			}
			else{
				
				r=rotateLL(r);
			}
		}
		//right heavy
		else if(hDiff>1){

			if(r.right.right==null){

				r=rotateRL(r);
			}
			else{

				r=rotateRR(r);
			}

		}
		
		return r;
	}

	//adds leafs to the tree (or increments occurrences of pre-existing leafs/parents)
	public void insert(E value){

		root=insertion(root, value);
		
	}

	private TreeNode insertion(TreeNode r, E value){

		//the value does not already exist in the tree
		if(r==null){		
			return (new TreeNode(value, null, null, 1,1));
		}

		int isEqual=r.data.compareTo(value);

		//this node is less than data, move right
		if(isEqual<0){

			r.right=insertion(r.right, value);
			r=balance(r);
		}
		//move left
		else if(isEqual>0){

			r.left=insertion(r.left, value);
			r=balance(r);
		}
		//the value already exists in the tree
		else{
			r.occurrences++;
		}

		return r;
	}

	//removes entirely or decreases occurence of leafs/parents that contain the parameter value 
	public void remove(E value){

		root=removeHelper(root, value);
	}

	//recursively finds and removes the node containing value
	private TreeNode removeHelper(TreeNode r, E value){

		//value to be removed was not found, or list is empty
		if(r==null)
			return null;

		int isEqual=r.data.compareTo(value);

		//the current node is less than value, need to move right
		if(isEqual<0){
			r.right=removeHelper(r.right, value);
		}
		//if the current node is greater than value, need to move left
		else if(isEqual>0){

			r.left=removeHelper(r.left, value);
		}
		//current node is the node to be removed
		else{
			r=removeNode(r);
		}

		return r;
	}

	//determines number of children and returns the state of the subtree now with that node parameter removed
	private TreeNode removeNode(TreeNode r){

		if(r.right==null && r.left==null){
			
		}
	
		else if(r.left==null){
			
			TreeNode toRemove=r;
			r=r.right;
			toRemove.right=null;
		}
		else if(r.right==null){
			
			TreeNode toRemove=r;
			r=r.left;
			toRemove.left=null;
		}
		else{
			
			TreeNode parent=null;
			TreeNode child=r.right;
			
			while(child.left!=null){
				parent=child;
				child=child.left;
			}
			
			if(parent==null){
				child=r.right;
			}
			else{
				parent.left=child.right;
				child.right=null;
			}
			
			r.data=child.data;
			r.occurrences=child.occurrences;
			
		}
		
		return r;
	}

	private void printDup(TreeNode r){

		for(int i=0;i<r.occurrences;i++){
			System.out.print(r.data);
		}
	}

	private void traverseIn(TreeNode r){

		if(r !=null){
			traverseIn(r.left);
			printDup(r);
			traverseIn(r.right);

		}
	}

	private void traversePost(TreeNode r){
		if(r !=null){
			traversePost(r.left);
			traversePost(r.right);
			printDup(r);
		}
	}

	private void traversePre(TreeNode r){
		if(r !=null){
			printDup(r);
			traversePre(r.left);
			traversePre(r.right);
		}
	}

	public void inOrder(){
		traverseIn(root);
	}

	public void postOrder(){
		traversePost(root);
	}

	public void preOrder(){
		traversePre(root);
	}

	//returns true if the tree contains the value
	public boolean contains(E value){

		return find(root, value);
	}

	private boolean find(TreeNode r, E value){

		//the tree is empty or the value is not found
		if(r==null)
			return false;

		int isEqual=r.data.compareTo(value);

		if(isEqual==0){
			return true;
		}
		else if(isEqual<0){
			return find(r.right, value);
		}
		else{
			return find(r.left, value);
		}
	}

	//returns the minimum value in the tree
	public E findMin(){

		return findMinHelper(root);
	}

	private E findMinHelper(TreeNode r){

		if(r == null)
			return null;
		if(r.left==null)
			return r.data;
		return findMinHelper(r.left);
	}

	//returns the maximum value in the tree
	public E findMax(){

		return findMaxHelper(root);
	}

	private E findMaxHelper(TreeNode r){

		if(r == null)
			return null;
		if(r.right==null)
			return r.data;

		return findMaxHelper(r.right);
	}

	//examines each node from left to right one level at a time. 
	public Iterator<E> levelOrder(){

		return new Iterator<E>(){

			private Queue<TreeNode> line;
			{
				line= new LinkedList<TreeNode>();

				if(root!=null){
					line.add(root);
				}
			}

			public boolean hasNext(){

				return(!line.isEmpty());
			}

			public E next(){

				TreeNode cur=line.remove();

				if(cur.left!=null){
					line.add(cur.left);
				}

				if(cur.right!=null){
					line.add(cur.right);
				}

				return cur.data;
			}

			public void remove(){
				throw new UnsupportedOperationException();
			}
		};
	}
}

/*		if(r.occurrences==1){

			//if the node to be removed has 2 children 
			if(r.right!=null && r.left!=null){

				TreeNode placeHolder=r;
				r=r.right;

				//checking to see if there are more suitable in order successors (looking for left most leaf in right subtree)
				while(r.left!=null && r.left.left!=null){
					r=r.left;
				}

				//not the immediate right of the node to be removed
				if(r.left!=null){

					//linking the in-order successor to the right and left of to-be-removed-node
					r.left.left=placeHolder.left;

					TreeNode right=r.left.right;
					r.left.right=placeHolder.right;

					//getting rid of the in-order successor's old links
					placeHolder=r.left;
					r.left=right;
					return placeHolder;
				}
				else{

					r.left=placeHolder.left;
				}
			}
			else if(r.right!=null){

				return r.right;
			}
			else if(r.left!=null){

				return r.left;
			}
			//the node to be removed has no children
			else{

				r=null;
			}
		}
		else{
			r.occurrences--;
		}

		return r;	
 * 
 * 
 */
