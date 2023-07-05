n = 1;
n_1 = 0;

i = 10;

while (i > 0) {
	temp = n + n_1;
	n_1 = n;
	n = temp;
	println n;

	i = i - 1;
}