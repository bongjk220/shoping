document.querySelectorAll("[data-panel-target]").forEach((button) => {
    button.addEventListener("click", () => {
        const target = button.dataset.panelTarget;

        document.querySelectorAll("[data-panel-target]").forEach((item) => {
            item.classList.remove("is-active");
        });

        document.querySelectorAll(".panel-view").forEach((panel) => {
            panel.classList.remove("is-active");
        });

        button.classList.add("is-active");

        const panel = document.getElementById(`panel-${target}`);
        if (panel) {
            panel.classList.add("is-active");
        }
    });
});
