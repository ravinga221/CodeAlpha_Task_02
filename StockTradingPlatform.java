import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class StockTradingPlatform 
{
	private Map<String, Stock> availableStocks;
    private Map<String, PortfolioItem> portfolio;
    private double cashBalance;
    private List<Transaction> transactionHistory;
    private LocalTime marketOpen;
    private LocalTime marketClose;
    private LocalDateTime currentTime;
	
	 public StockTradingPlatform() 
	{
        initializeStocks();
        this.portfolio = new HashMap<>();
        this.cashBalance = 10000.00;
        this.transactionHistory = new ArrayList<>();
        this.marketOpen = LocalTime.of(9, 30);
        this.marketClose = LocalTime.of(16, 0);
        this.currentTime = LocalDateTime.now();
    }
}