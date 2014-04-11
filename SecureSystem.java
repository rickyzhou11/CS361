import java.io.*;

import java.util.*;
//Chuyu Zhou CZ568 rickyzhou@utexas.edu

public class SecureSystem {

	HashMap<String, Integer> subj;
	 HashMap<String, Integer> obj;
	 HashMap<String, SecurityLevel> subjLvl;
	 HashMap<String, SecurityLevel> objLvl;


	public SecureSystem () {
		subj = new HashMap<String, Integer>();
		obj = new HashMap<String, Integer>();
		subjLvl = new HashMap<String, SecurityLevel>();
		objLvl = new HashMap<String, SecurityLevel>();
	}


	public static void main (String args[]) throws IOException {
		File f;
		FileReader readFile;
		BufferedReader ReadBuff;
		String line;
		SecureSystem sys;
		Instruct i;

		  // LOW and HIGH are constants defined in the SecurityLevel 
        // class, such that HIGH dominates LOW.

	//SecurityLevel low  = SecurityLevel.LOW;
	//SecurityLevel high = SecurityLevel.HIGH;

	// We add two subjects, one high and one low.

		sys = new SecureSystem();

		sys.createSubject("lyle", SecurityLevel.low);
		sys.createSubject("hal", SecurityLevel.high);

		sys.createNewObject("lobj", SecurityLevel.low);
		sys.createNewObject("hobj", SecurityLevel.high);

		f = new File(args[0]);
		readFile = new FileReader(f);
		ReadBuff = new BufferedReader(readFile);

		while ((line = ReadBuff.readLine()) != null) {
			i = new Instruct(line);

			if (i.valid()) {
				switch (i.readOp()) {
					case read:
						sys.read(i.readSub(), i.readObj());
						break;
					case write:
						sys.write(i.readSub(), i.readObj(), i.readVal());
						break;
				}
			}

			System.out.println(i);
			System.out.println(sys);
		}
	}

	public void createSubject (String n, SecurityLevel l) {
		this.subj.put(n, 0);
		this.subjLvl.put(n, l);
	}

	public void createNewObject (String n, SecurityLevel l) {
		this.obj.put(n, 0);
		this.objLvl.put(n, l);
	}

	public Integer read (String s, String o) {
		Integer sLevel;
		Integer oLevel;
		Integer oValue;

		if (!this.subjLvl.containsKey(s) || !this.objLvl.containsKey(o)) {
			return 0;
		}

		sLevel = this.subjLvl.get(s).level();
		oLevel = this.objLvl.get(o).level();

		if (sLevel < oLevel) {
			return 0;
		}

		oValue = this.obj.get(o);

		this.subj.put(s, oValue);

		return oValue;
	}

	public void write (String s, String o, Integer v) {
		Integer sLevel;
		Integer oLevel;

		if (!this.subjLvl.containsKey(s) || !this.objLvl.containsKey(o)) {
			return;
		}

		sLevel = this.subjLvl.get(s).level();
		oLevel = this.objLvl.get(o).level();

		if (sLevel > oLevel) {
			return;
		}

		this.obj.put(o, v);
	}

	public String toString () {
		String res;
		Set k;
		Iterator i;

		res = "The current state is:\n";

		k = this.obj.keySet();
		i = k.iterator();
		while (i.hasNext()) {
			Object next = i.next();
			res += " " + next + "has value: " + this.obj.get(next) + "\n";
		}

		k = this.subj.keySet();
		i = k.iterator();
		while (i.hasNext()) {
			Object next = i.next();
			res += " " + next + "has recently read: " + this.subj.get(next) + "\n";
		}

		return res;
	}
}

class Instruct {


	private String line;
	private Worker oper;
	private String subj;
	private String obj;
	private Integer value;
	private boolean invalid;


	public Instruct (String l) {
		String[] m;

		this.line = l;
		m = this.line.toLowerCase().trim().split("\\s+");

		try {
			this.oper = Worker.valueOf(m[0]);

			if (m.length - 1 != oper.args()) {
				this.invalid = true;
			}

			switch (this.oper) {
				case read:
					this.subj = m[1];
					this.obj = m[2];
					break;
				case write:
					this.subj = m[1];
					this.obj = m[2];
					this.value = value.parseInt(m[3]);
					break;
				
			}
		}
		catch (IllegalArgumentException e) {
			this.invalid = true;
		}
		catch (ArrayIndexOutOfBoundsException e) {
			this.invalid = true;
		}
	}

	public boolean valid () { return !this.invalid; }

	public Worker readOp () { return this.oper; }
	public String readSub () { return this.subj; }
	public String readObj () { return this.obj; }
	public Integer readVal () { return this.value; }

	public void writeOp (Worker x) { this.oper = x; }
	public void writeSub (String x) { this.subj = x; }
	public void writeObj (String x) { this.obj = x; }
	public void writeVal (Integer x) { this.value = x; }

	public String toString () {
		if (this.invalid) {
			return "Bad instruction ";
		}

		switch (this.oper) {
			case read:
				return this.subj + " reads " + this.obj;
			case write:
				return this.subj + " writes " + this.value + " to " + this.obj;
			default:
				return "Bad instruction";
		}
	}
}

enum Worker {
	read (2),
	write (3);

	final Integer x;

	Worker (Integer args) { this.x = args; }


	Integer args () { return this.x; }
}

enum SecurityLevel {
	low (1),
	high (2);

	final Integer x;

	SecurityLevel (Integer level) { this.x = level; }

	Integer level () { return this.x; }
}
