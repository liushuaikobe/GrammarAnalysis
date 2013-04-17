int main()
{
	int i = 0, b[10] = {100};
	while (i < 10) {
		if ( i != 5 || i == 19) {
			break;
		} else {
			i = i + 23;
			continue;
		}
		i = i + 1;
		b[0] = 23;
	}
	return i + 1;
}