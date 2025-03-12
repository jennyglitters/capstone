document.addEventListener("DOMContentLoaded", function () {
    // Fetch and update total fish count
    fetch('/fish/total')
        .then(response => response.json())
        .then(totalFish => {
            let totalFishElement = document.getElementById('total-fish');
            if (totalFishElement) {
                totalFishElement.textContent = totalFish.toLocaleString();
            } else {
                console.error("Error: Cannot find element with ID 'total-fish'");
            }
        })
        .catch(error => console.error("Error fetching total fish:", error));

    // Fetch and update active tank count
    fetch('/tank/count')
        .then(response => response.json())
        .then(activeTanks => {
            let activeTanksElement = document.getElementById('active-tanks');
            if (activeTanksElement) {
                activeTanksElement.textContent = activeTanks;
            } else {
                console.error("Error: Cannot find element with ID 'active-tanks'");
            }
        })
        .catch(error => console.error("Error fetching active tanks:", error));

    // Fetch and update fish count per tank for the chart
    fetch('/fish/count-per-tank')
        .then(response => response.json())
        .then(data => {
            let tankLabels = data.map(tank => "Tank " + tank.tankId); // Ensure correct key
            let fishCounts = data.map(tank => tank.totalFish);

            let ctx = document.getElementById("fishChart");

            if (ctx) { // Ensure element exists before trying to use getContext
                new Chart(ctx.getContext("2d"), {
                    type: "bar",
                    data: {
                        labels: tankLabels,
                        datasets: [{
                            label: "Number of Fish per Tank",
                            data: fishCounts,
                            backgroundColor: "rgba(54, 162, 235, 0.6)",
                            borderColor: "rgba(54, 162, 235, 1)",
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            y: {
                                beginAtZero: true
                            }
                        }
                    }
                });
            } else {
                console.error("Error: Cannot find element with ID 'fishChart'");
            }
        })
        .catch(error => console.error("Error fetching fish count data:", error));
});

