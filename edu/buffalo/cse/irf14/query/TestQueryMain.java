package edu.buffalo.cse.irf14.query;

import java.util.HashSet;

public class TestQueryMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Query query = new Query("{ Term:Term1 }");
		HashSet<Integer> set = query.getQueryDocIdSet();
		System.out.println(set);
	}

}
