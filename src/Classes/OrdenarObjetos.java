package Classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Objects.Gson.Cliente;
import Objects.Gson.Enlace;
import Objects.Gson.Foro;

public class OrdenarObjetos {

	public OrdenarObjetos() {
		super();
	}
	

	
	
	
	//Array de clientes -----------------------------------------------------------------------------------------------------------------------------------------------------------
	public void clientesByDomain(ArrayList<Cliente> clientes) {
		Collections.sort(clientes, new Comparator<Cliente>() {
			@Override
			public int compare(Cliente c1, Cliente c2) {
				return c1.getDominio().compareTo(c2.getDominio());
			}
		});
	}
	
	public void clientesByDomainDes(ArrayList<Cliente> clientes) {
		Collections.sort(clientes, new Comparator<Cliente>() {
			@Override
			public int compare(Cliente c1, Cliente c2) {
				return c2.getDominio().compareTo(c1.getDominio());
			}
		});
	}
	
	public void clientesByName(ArrayList<Cliente> clientes) {
		Collections.sort(clientes, new Comparator<Cliente>() {
			@Override
			public int compare(Cliente c1, Cliente c2) {
				return c1.getNombre().compareTo(c2.getNombre());
			}
		});
		
	}
	
	public void clientesByNameDes(ArrayList<Cliente> clientes) {
		Collections.sort(clientes, new Comparator<Cliente>() {
			@Override
			public int compare(Cliente c1, Cliente c2) {
				return c2.getNombre().compareTo(c1.getNombre());
			}
		});
		
	}
	
	public void clientesByServicio(ArrayList<Cliente> clientes) {
		Collections.sort(clientes, new Comparator<Cliente>() {
			@Override
			public int compare(Cliente c1, Cliente c2) {
				return c1.getServicio().compareTo(c2.getServicio());
			}
		});		
	}

	public void clientesByServicioDes(ArrayList<Cliente> clientes) {
		Collections.sort(clientes, new Comparator<Cliente>() {
			@Override
			public int compare(Cliente c1, Cliente c2) {
				return c2.getServicio().compareTo(c1.getServicio());
			}
		});
		
	}

	public void clientesByUser(ArrayList<Cliente> clientes) {
		Collections.sort(clientes, new Comparator<Cliente>() {
			@Override
			public int compare(Cliente c1, Cliente c2) {
				return c1.getName_empleado().compareTo(c2.getName_empleado());
			}
		});	
	}

	public void clientesByUserDes(ArrayList<Cliente> clientes) {
		Collections.sort(clientes, new Comparator<Cliente>() {
			@Override
			public int compare(Cliente c1, Cliente c2) {
				return c2.getName_empleado().compareTo(c1.getName_empleado());
			}
		});
	}


	
	//Array de foros --------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void forosByWeb(ArrayList<Foro> foros) {
		Collections.sort(foros, new Comparator<Foro>() {
			@Override
			public int compare(Foro f1, Foro f2) {
				return f1.getWebForo().compareTo(f2.getWebForo());
			}
		});
	}
	
	//Array de enlaces -----------------------------------------------------------------------------------------------------------------------------------------------------------
	public void enlacesByTipo(ArrayList<Enlace> enlaces) {
		Collections.sort(enlaces, new Comparator<Enlace>() {
			@Override
			public int compare(Enlace e1, Enlace e2) {
				return e1.getTipo().compareTo(e2.getTipo());
			}
		});
	}









	




	


	

}
