package com.boostmytool.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boostmytool.model.customers.Customer;
import com.boostmytool.model.orders.Order;
import com.boostmytool.model.products.Product;
import com.boostmytool.model.suppliers.*;
import com.boostmytool.service.customers.*;
import com.boostmytool.service.employee.*;
import com.boostmytool.service.orders.*;
import com.boostmytool.service.products.*;
import com.boostmytool.service.suppliers.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Controller
@RequestMapping("/admin")
public class HomeController {
	
	@Autowired
    private ProductService productService;
	
	@Autowired
    private OrdersService orderService;
	
	@Autowired
    private SupplierService supplierService;
	
	@Autowired
    private CustomerService customerService;
	
	@Autowired
    private EmployeeService employeeService;
	
	@Autowired
    private ProductsRepository repoP;
	
	@Autowired
	private CustomerRepository repoC;
	
	@Autowired
	private OrdersRepository repoO;
	
	@Autowired
    private SuppliersRepository repoS;
	
	@GetMapping({"","/"})
	public String home() {	
		return "/index";
	}
	@GetMapping("/home")
	public String admin(Model model) {
		String showTableValue = "false";
		model.addAttribute("showTable", showTableValue);
		model.addAttribute("viewsuppliers","false");
		model.addAttribute("vieworders","false");
		model.addAttribute("viewproducts","false");
		model.addAttribute("viewcustomers","false");
		model.addAttribute("totalNumberProduct", productService.totalNumberProduct());
		model.addAttribute("totalNumberSupplier", supplierService.totalNumberSupplier());
		model.addAttribute("totalNumberCustomer", customerService.totalNumberCustomer());
		model.addAttribute("totalNumberEmployee", employeeService.totalNumberEmployee());
		model.addAttribute("totalNumberOrder", orderService.totalNumberOrder());
		Product[] topProducts = productService.topSelling();
		// Gán giá trị cho top sản phẩm, gán null nếu không đủ
	    model.addAttribute("top1Product", topProducts.length > 0 ? topProducts[0] : null);
	    model.addAttribute("top2Product", topProducts.length > 1 ? topProducts[1] : null);
	    model.addAttribute("top3Product", topProducts.length > 2 ? topProducts[2] : null);
	    Supplier[] topSuppliers = supplierService.topSelling();
		// Gán giá trị cho top nhà cung cấp, gán null nếu không đủ
	    model.addAttribute("top1Supplier", topSuppliers.length > 0 ? topSuppliers[0] : null);
	    model.addAttribute("top2Supplier", topSuppliers.length > 1 ? topSuppliers[1] : null);
	    model.addAttribute("top3Supplier", topSuppliers.length > 2 ? topSuppliers[2] : null);
	    Customer[] topCustomers = customerService.topSelling();
		// Gán giá trị cho top nhà cung cấp, gán null nếu không đủ
	    model.addAttribute("top1Customer", topCustomers.length > 0 ? topCustomers[0] : null);
	    model.addAttribute("top2Customer", topCustomers.length > 1 ? topCustomers[1] : null);
	    model.addAttribute("top3Customer", topCustomers.length > 2 ? topCustomers[2] : null);
		return "admin/index";
	}
	
	@GetMapping("/showviewtable")
	public String showViewTable(Model model, @RequestParam String table) {
		
		//Mở Bảng
		String showTableValue = "true";
		model.addAttribute("showTable", showTableValue);
		//Reset các bảng
		model.addAttribute("viewsuppliers","false");
		model.addAttribute("vieworders","false");
		model.addAttribute("viewproducts","false");
		model.addAttribute("viewcustomers","false");
		//Nạp tất cả các đối tượng
		List<Product> products = repoP.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("products", products);
		
		List<Order> orders = repoO.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("orders", orders);
		
		List<Customer> customers = repoC.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("customers", customers);
		
		List<Supplier> suppliers = repoS.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("suppliers", suppliers);
		
		if(table.equals("customers")) {
			model.addAttribute("viewcustomers","true");
		}else {
			model.addAttribute("viewcustomers","false");
		}
		if(table.equals("products")) {
			model.addAttribute("viewproducts","true");
		}else {
			model.addAttribute("viewproducts","false");
		}
		if(table.equals("orders")) {
			model.addAttribute("vieworders","true");
		}else {
			model.addAttribute("vieworders","false");
		}
		if(table.equals("suppliers")) {
			model.addAttribute("viewsuppliers","true");
		}else {
			model.addAttribute("viewsuppliers","false");
		}
		model.addAttribute("totalNumberProduct", productService.totalNumberProduct());
		model.addAttribute("totalNumberSupplier", supplierService.totalNumberSupplier());
		model.addAttribute("totalNumberCustomer", customerService.totalNumberCustomer());
		model.addAttribute("totalNumberEmployee", employeeService.totalNumberEmployee());
		model.addAttribute("totalNumberOrder", orderService.totalNumberOrder());
		Product[] topProducts = productService.topSelling();
		// Gán giá trị cho top sản phẩm, gán null nếu không đủ
	    model.addAttribute("top1Product", topProducts.length > 0 ? topProducts[0] : null);
	    model.addAttribute("top2Product", topProducts.length > 1 ? topProducts[1] : null);
	    model.addAttribute("top3Product", topProducts.length > 2 ? topProducts[2] : null);
	    Supplier[] topSuppliers = supplierService.topSelling();
		// Gán giá trị cho top nhà cung cấp, gán null nếu không đủ
	    model.addAttribute("top1Supplier", topSuppliers.length > 0 ? topSuppliers[0] : null);
	    model.addAttribute("top2Supplier", topSuppliers.length > 1 ? topSuppliers[1] : null);
	    model.addAttribute("top3Supplier", topSuppliers.length > 2 ? topSuppliers[2] : null);
	    Customer[] topCustomers = customerService.topSelling();
		// Gán giá trị cho top nhà cung cấp, gán null nếu không đủ
	    model.addAttribute("top1Customer", topCustomers.length > 0 ? topCustomers[0] : null);
	    model.addAttribute("top2Customer", topCustomers.length > 1 ? topCustomers[1] : null);
	    model.addAttribute("top3Customer", topCustomers.length > 2 ? topCustomers[2] : null);
		return "admin/index";
	}
	
	
	@GetMapping("/search")
	public String search(Model model) {
		String showTableValue = "false";
		model.addAttribute("showTable", showTableValue);
		return "admin/AdminSearch";
	}
	
	@GetMapping("/searchcatalogprice")
	public String searchProducts(
	    @RequestParam String category,
	    @RequestParam float minPrice,
	    @RequestParam float maxPrice,
	    @RequestParam(required = false) String showTable, 
	    Model model
	) {
		List<Product> products = productService.searchProducts(category, minPrice, maxPrice);
		model.addAttribute("showTable", showTable != null && showTable.equals("true"));
		model.addAttribute("products", products);
		return "admin/AdminSearch";
	}
	
	@GetMapping("/statistic")
	public String statistic(Model model) throws JsonProcessingException {
	    Map<String, double[]> stats = orderService.getMonthlyYearlyStats();
	    double[] monthlyCosts = stats.get("monthlyCosts");
	    double[] monthlyPrices = stats.get("monthlyPrices");
	    double[] yearlyCosts = stats.get("yearlyCosts");
	    double[] yearlyPrices = stats.get("yearlyPrices");

	    // Tính tổng chi phí và doanh thu
	    double totalMonthlyCosts = 0;
	    double totalMonthlyRevenue = 0;
	    double totalYearlyCosts = 0;
	    double totalYearlyRevenue = 0;

	    for (double cost : monthlyCosts) {
	        totalMonthlyCosts += cost;
	    }

	    System.out.println("Total Monthly Costs: " + totalMonthlyCosts);
	    for (double price : monthlyPrices) {
	        totalMonthlyRevenue += price;
	    }

	    for (double cost : yearlyCosts) {
	        totalYearlyCosts += cost;
	    }

	    for (double price : yearlyPrices) {
	        totalYearlyRevenue += price;
	    }

	    // Tính lợi nhuận
	    double profitMonthly = totalMonthlyRevenue - totalMonthlyCosts;
	    double profitYearly = totalYearlyRevenue - totalYearlyCosts;

	    // Thêm dữ liệu vào model
	    ObjectMapper objectMapper = new ObjectMapper();
	    String monthlyCostsJson = objectMapper.writeValueAsString(monthlyCosts);
	    model.addAttribute("monthlyCosts", monthlyCostsJson);
	    String monthlyPricesJson = objectMapper.writeValueAsString(monthlyPrices);
	    model.addAttribute("monthlyPrices", monthlyPricesJson);
	    model.addAttribute("totalMonthlyCosts", totalMonthlyCosts);
	    model.addAttribute("totalMonthlyRevenue", totalMonthlyRevenue);
	    model.addAttribute("profitMonthly", profitMonthly);

	    String yearlyCostsJson = objectMapper.writeValueAsString(yearlyCosts);
	    model.addAttribute("yearlyCosts", yearlyCostsJson);
	    String yearyPricesJson = objectMapper.writeValueAsString(yearlyPrices);
	    model.addAttribute("yearlyPrices", yearyPricesJson);
	    model.addAttribute("totalYearlyCosts", totalYearlyCosts);
	    model.addAttribute("totalYearlyRevenue", totalYearlyRevenue);
	    model.addAttribute("profitYearly", profitYearly);

	    return "admin/static/index";
	}
	
	@GetMapping("/productview")
	public String productView(
	    @RequestParam(required = false) String showTable, 
	    Model model
	) {
		return "admin/products/index";
	}
	@GetMapping("/customerview")
	public String customerView(
	    @RequestParam(required = false) String showTable, 
	    Model model
	) {
		return "admin/customers/index";
	}
	@GetMapping("/supplierview")
	public String supplierView(
	    @RequestParam(required = false) String showTable, 
	    Model model
	) {
		return "admin/suppliers/index";
	}
	@GetMapping("/orderview")
	public String orderView(
	    @RequestParam(required = false) String showTable, 
	    Model model
	) {
		return "admin/orders/index";
	}
}
