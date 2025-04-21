# Set a seed for reproducibility
set.seed(1234)

# Load the CSV file (adjust the filename/path if necessary)
data <- read.csv("simulation_events_nums.csv", header = TRUE)

# Replace "norm1" with the actual column name that contains the normal sample
sample_data <- data$norm1

# Ensure that the data are numeric
sample_data <- as.numeric(as.character(sample_data))

# Optionally, if there are many ties due to rounding, add a small jitter:
sample_data <- jitter(sample_data, factor = 1e-6)

# Define theoretical normal distribution parameters:
mu <- 2       # Theoretical mean
sigma <- 0.3  # Theoretical standard deviation

# -------------------------------
# 1. Histogram with Theoretical Normal Density
# -------------------------------
hist(sample_data, breaks = 30, probability = TRUE,
     main = "Normal Distribution (mean = 2, sd = 0.3)",
     xlab = "x", col = "mistyrose", border = "gray")
curve(dnorm(x, mean = mu, sd = sigma), 
      from = min(sample_data), to = max(sample_data),
      col = "red", lwd = 2, add = TRUE)

# -------------------------------
# 2. Compare Empirical and Theoretical Parameters
# -------------------------------
empirical_mean <- mean(sample_data)
empirical_var  <- var(sample_data)
theoretical_var <- sigma^2

cat("Normal Distribution Test\n")
cat("------------------------\n")
cat("Theoretical Mean:", mu, "\n")
cat("Empirical Mean  :", empirical_mean, "\n")
cat("\n")
cat("Theoretical Variance:", theoretical_var, "\n")
cat("Empirical Variance  :", empirical_var, "\n\n")

# -------------------------------
# 3. Kolmogorov–Smirnov Test
# -------------------------------
# H0: The sample follows a N(mu = 2, sd = 0.3) distribution.
# Ha: The sample distribution is significantly different.
# Significance level alpha = 0.05.
ks_result <- ks.test(sample_data, "pnorm", mean = mu, sd = sigma)

cat("====================================================\n")
cat("Kolmogorov–Smirnov Test for Normal Distribution\n")
cat("====================================================\n")
cat("Null hypothesis (H0): The sample follows N(mean =", mu, ", sd =", sigma, ")\n")
cat("Alternative (Ha): The sample distribution is significantly different.\n")
cat("Significance level (alpha) = 0.05\n\n")
cat("K-S test statistic:", ks_result$statistic, "\n")
cat("p-value:", ks_result$p.value, "\n\n")

if (ks_result$p.value < 0.05) {
  cat("Conclusion:\n")
  cat("  p-value < alpha => Reject H0. The sample does NOT follow N(mean =", mu, ", sd =", sigma, ")\n\n")
} else {
  cat("Conclusion:\n")
  cat("  p-value >= alpha => Fail to reject H0. The sample appears to follow N(mean =", mu, ", sd =", sigma, ")\n\n")
}

# -------------------------------
# 4. Chi-square Test
# -------------------------------
# Bin the data to compare observed frequencies with the expected ones.
num_bins <- 10
breaks_norm <- quantile(sample_data, probs = seq(0, 1, length.out = num_bins + 1))
obs_freq <- hist(sample_data, breaks = breaks_norm, plot = FALSE)$counts
n <- length(sample_data)

# Compute expected frequencies from the theoretical normal CDF
expected_freq <- numeric(num_bins)
for (i in 1:num_bins) {
  p_lower <- pnorm(breaks_norm[i], mean = mu, sd = sigma)
  p_upper <- pnorm(breaks_norm[i+1], mean = mu, sd = sigma)
  expected_freq[i] <- n * (p_upper - p_lower)
}

# Normalize expected frequencies to get probabilities (they must sum to 1)
p_expected <- expected_freq / sum(expected_freq)

cat("========================================\n")
cat("Chi-square Test for Normal Distribution\n")
cat("========================================\n")
cat("Null hypothesis (H0): Observed frequencies match expected frequencies\n")
cat("  (i.e., the sample follows N(mean =", mu, ", sd =", sigma, "))\n")
cat("Significance level (alpha) = 0.05\n\n")
cat("Observed frequencies: ", obs_freq, "\n")
cat("Expected frequencies: ", expected_freq, "\n\n")

chisq_result <- chisq.test(x = obs_freq, p = p_expected)
cat("Chi-square test statistic:", chisq_result$statistic, "\n")
cat("Degrees of freedom:", chisq_result$parameter, "\n")
cat("p-value:", chisq_result$p.value, "\n\n")

if (chisq_result$p.value < 0.05) {
  cat("Conclusion:\n")
  cat("  p-value < alpha => Reject H0. The sample distribution does NOT match N(mean =", mu, ", sd =", sigma, ")\n")
} else {
  cat("Conclusion:\n")
  cat("  p-value >= alpha => Fail to reject H0. The sample distribution is consistent with N(mean =", mu, ", sd =", sigma, ")\n")
}
